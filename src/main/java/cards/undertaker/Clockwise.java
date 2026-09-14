package cards.undertaker;

import actions.ClockwiseSequencer;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Clockwise extends CustomCard {
    public static final String ID = "Clockwise";
    private static final String IMG_PATH = "img/cards/undertaker/Clockwise.png";
    private static final int COST = 3;
    private static final int PLAY_COUNT = 3;
    private static final int UPGRADE_PLUS_PLAY_COUNT = 1;

    public Clockwise() {
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
        ClockwiseSequencer.request(this.magicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Clockwise();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PLAY_COUNT);
            initializeDescription();
        }
    }
}
