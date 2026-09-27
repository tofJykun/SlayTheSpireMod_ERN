package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;

public class AlabasterLordsPull extends CustomCard {
    public static final String ID = "AlabasterLordsPull";
    public static final int HP_LOSS = 4;

    public AlabasterLordsPull() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/AlabasterLordsPull.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new LoseHPAction(monster, p, HP_LOSS));
            }
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, magicNumber, false), magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AlabasterLordsPull();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
