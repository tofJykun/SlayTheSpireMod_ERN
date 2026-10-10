package cards.executor;

import actions.BouquetStrengthLossAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Bully extends CustomCard {
    public static final String ID = "Bully";
    private static final int HEALTH_PERCENT = 50;

    public Bully() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/Bully.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION
                        .replace("!MD!", Integer.toString(HEALTH_PERCENT)),
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 4;
        this.baseMagicNumber = this.magicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster == null) {
            return;
        }
        boolean highHealth = monster.maxHealth > 0
                && (long)monster.currentHealth * 100 >= (long)monster.maxHealth * HEALTH_PERCENT;
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (highHealth) {
            addToBot(new BouquetStrengthLossAction(monster, player, this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bully();
    }
}
