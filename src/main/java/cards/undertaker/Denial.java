package cards.undertaker;

import actions.DenialAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Denial extends CustomCard {
    public static final String ID = "Denial";
    private static final String IMG_PATH = "img/cards/undertaker/Denial.png";
    private static final int COST = 1;
    private static final int CARDS_TO_RETURN = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;

    public Denial() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = CARDS_TO_RETURN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DenialAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Denial();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARDS);
        }
    }
}
