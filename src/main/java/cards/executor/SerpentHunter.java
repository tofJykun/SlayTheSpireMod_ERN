package cards.executor;

import actions.SerpentHunterAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class SerpentHunter extends CustomCard {
    public static final String ID = "SerpentHunter";
    private static final String IMG_PATH = "img/cards/executor/SerpentHunter.png";
    private static final int COST = -1;

    public SerpentHunter() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int plays = this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            plays += 2;
            p.getRelic("Chemical X").flash();
        }
        if (this.upgraded) {
            plays++;
        }
        addToBot(new SerpentHunterAction(Math.max(0, plays), getCardStrings().EXTENDED_DESCRIPTION[0]));
        // X is paid even when there is no attack to select.
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SerpentHunter();
    }
}
