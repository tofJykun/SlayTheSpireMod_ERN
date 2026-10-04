package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.SpiritPower;

public class SpiritOfAsh extends CustomCard {
    public static final String ID = "SpiritOfAsh";
    private static final String IMG_PATH = "img/cards/revenant/SpiritOfAsh.png";

    public SpiritOfAsh() {
        super(ID, strings().NAME, IMG_PATH, -1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            x += 2;
            p.getRelic("Chemical X").flash();
        }
        if (x > 0) {
            addToBot(new ApplyPowerAction(p, p, new SpiritPower(p, x), x));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpiritOfAsh();
    }
}
