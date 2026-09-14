package cards.undertaker;

import actions.ClockwiseSequencer;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class CounterClockwise extends CustomCard {
    public static final String ID = "CounterClockwise";
    private static final String IMG_PATH = "img/cards/undertaker/CounterClockwise.png";
    private static final int COST = 2;
    private static final int PLAY_COUNT = 2;
    private static final int UPGRADE_COST = 1;

    public CounterClockwise() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = PLAY_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ClockwiseSequencer.requestRight(this.magicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CounterClockwise();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
