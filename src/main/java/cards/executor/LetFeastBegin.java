package cards.executor;

import actions.LetFeastBeginAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class LetFeastBegin extends CustomCard {
    public static final String ID = "LetFeastBegin";
    private static final String IMG_PATH = "img/cards/executor/LetFeastBegin.png";
    private static final int COST = 1;
    private static final int MAX_HP_GAIN = 3;
    private static final int UPGRADE_PLUS_MAX_HP_GAIN = 2;

    public LetFeastBegin() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = MAX_HP_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new LetFeastBeginAction(p, m, this.magicNumber));
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = hasExecutableEnemy()
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private static boolean hasExecutableEnemy() {
        if (AbstractDungeon.player == null
                || AbstractDungeon.getCurrRoom() == null
                || AbstractDungeon.getCurrRoom().monsters == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped() && monster.maxHealth <= AbstractDungeon.player.maxHealth) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new LetFeastBegin();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAX_HP_GAIN);
            initializeDescription();
        }
    }
}
