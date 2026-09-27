package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class WhiteLightCharge extends AbstractScheduledCard {
    public static final String ID = "WhiteLightCharge";
    private static final String IMG_PATH = "img/cards/duchess/WhiteLightCharge.png";
    private static final int COST = 2;
    private static final int DRAW = 3;
    private static final int UPGRADE_PLUS_DRAW = 1;
    private static final int DEFAULT_SCHEDULED = 4;

    public WhiteLightCharge() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF,
                DEFAULT_SCHEDULED);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction) new DrawCardAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhiteLightCharge();
    }
}
