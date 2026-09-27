package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class GoldenParry extends CustomCard {
    public static final String ID = "GoldenParry";
    private static final String IMG_PATH = "img/cards/undertaker/GoldenParry.png";
    private static final int COST = -1;
    private static final int BLOCK = 12;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public GoldenParry() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = getXValue(p);
        for (int i = 0; i < x; i++) {
            addToBot(new GainBlockAction(p, p, this.block));
        }
        addToBot(new PressEndTurnButtonAction());
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    private int getXValue(AbstractPlayer p) {
        int value = this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse;
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
            p.getRelic("Chemical X").flash();
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new GoldenParry();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
