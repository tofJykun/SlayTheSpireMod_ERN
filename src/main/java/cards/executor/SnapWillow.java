package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SnapWillow extends CustomCard {
    public static final String ID = "SnapWillow";
    private static final int HEALTH_PERCENT = 50;

    public SnapWillow() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/SnapWillow.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION
                        .replace("!MD!", Integer.toString(HEALTH_PERCENT)),
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 6;
        this.baseMagicNumber = this.magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Snapshot before this attack or the subsequent draw changes the target's HP.
        boolean gainEnergy = monster != null && monster.maxHealth > 0
                && (long)monster.currentHealth * 100 >= (long)monster.maxHealth * HEALTH_PERCENT;
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DrawCardAction(player, this.magicNumber));
        if (gainEnergy) {
            addToBot(new GainEnergyAction(2));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SnapWillow();
    }
}
