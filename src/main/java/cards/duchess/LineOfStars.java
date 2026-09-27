package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import patches.AbstractCardEnum;

public class LineOfStars extends CustomCard {
    public static final String ID = "LineOfStars";
    private static final String IMG_PATH = "img/cards/duchess/LineOfStars.png";

    public LineOfStars() {
        super(ID, strings().NAME, IMG_PATH, 1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cardsInHand = 0;
        // use() runs before the played card leaves the hand; autoplay may already have moved it.
        for (AbstractCard card : p.hand.group) {
            if (card != this) {
                cardsInHand++;
            }
        }
        if (cardsInHand > 0) {
            addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, cardsInHand), cardsInHand));
        }
        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LineOfStars();
    }
}
