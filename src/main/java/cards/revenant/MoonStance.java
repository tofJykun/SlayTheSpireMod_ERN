package cards.revenant;

import basemod.abstracts.CustomCard;
import cards.tempcards.MoonlightSettles;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class MoonStance extends CustomCard {
    public static final String ID = "MoonStance";
    private static final String IMG_PATH = "img/cards/revenant/MoonStance.png";

    public MoonStance() {
        super(ID, strings().NAME, IMG_PATH, -1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
        this.cardsToPreview = new MoonlightSettles();
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private int generatedEnergy(AbstractPlayer player, int x) {
        return Math.max(0, x) + (this.upgraded ? 1 : 0)
                + (player != null && player.hasRelic("Chemical X") ? 2 : 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            p.getRelic("Chemical X").flash();
        }
        addToBot(new MakeTempCardInHandAction(new MoonlightSettles(generatedEnergy(p, x)), 1));
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updatePreview();
    }

    private void updatePreview() {
        ((MoonlightSettles)this.cardsToPreview).setX(generatedEnergy(AbstractDungeon.player,
                AbstractDungeon.player == null ? 0 : EnergyPanel.totalCount));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
            updatePreview();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MoonStance();
    }
}
