package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ScheduledField;

public class ScrapsOfLife extends AbstractScheduledCard {
    public static final String ID = "ScrapsOfLife";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public ScrapsOfLife() {
        super(ID, STRINGS.NAME, "img/cards/duchess/ScrapsOfLife.png", -2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY, 3);
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (ScheduledField.isPendingAutoplay(this)) return super.canUse(p, m);
        cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new LoseHPAction(monster, p, magicNumber));
            }
        }
        addToBot(new ExhaustAction(1, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
            ScheduledField.setBaseScheduled(this, 2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScrapsOfLife();
    }
}
