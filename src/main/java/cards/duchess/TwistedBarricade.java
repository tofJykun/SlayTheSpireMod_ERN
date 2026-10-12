package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class TwistedBarricade extends AbstractScheduledCard {
    public static final String ID = "TwistedBarricade";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public TwistedBarricade() {
        super(ID, STRINGS.NAME, "img/cards/duchess/TwistedBarricade.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.BASIC, CardTarget.SELF, 2);
        baseBlock = 7;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        baseMagicNumber = base;
        magicNumber = current;
        isMagicNumberModified = current != base;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TwistedBarricade();
    }
}
