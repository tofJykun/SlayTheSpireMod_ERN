package cards.duchess;

import actions.InvertedStatueAction;
import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class InvertedStatue extends AbstractScheduledCard {
    public static final String ID = "InvertedStatue";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public InvertedStatue() {
        super(ID, STRINGS.NAME, "img/cards/duchess/InvertedStatue.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF, 3);
        baseBlock = 8;
        onScheduledCountChanged(3, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new InvertedStatueAction(p));
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
            upgradeBlock(4);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new InvertedStatue(); }
}
