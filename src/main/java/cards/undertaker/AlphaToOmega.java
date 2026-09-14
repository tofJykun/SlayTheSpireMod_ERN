package cards.undertaker;

import actions.AlphaToOmegaSequencer;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class AlphaToOmega extends CustomCard {
    public static final String ID = "AlphaToOmega";
    private static final String IMG_PATH = "img/cards/undertaker/AlphaToOmega.png";
    private static final int COST = 4;
    private static final int UPGRADE_COST = 3;

    public AlphaToOmega() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AlphaToOmegaSequencer.request(this);
    }

    @Override
    public AbstractCard makeCopy() {
        return new AlphaToOmega();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
