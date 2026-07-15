package cards.recluse;

import basemod.abstracts.CustomCard;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class LawOfRegression extends CustomCard {
    public static final String ID = "LawOfRegression";
    private static final String IMG_PATH = "img/cards/recluse/LawOfRegression.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int HAND_SIZE = 10;

    public LawOfRegression() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        this.cardsToPreview = new FadingPrimalGlintstone();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cardsToCreate = HAND_SIZE - p.hand.size();
        if (cardsToCreate > 0) {
            addToBot((AbstractGameAction)new MakeTempCardInHandAction(new FadingPrimalGlintstone(), cardsToCreate));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new LawOfRegression();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
