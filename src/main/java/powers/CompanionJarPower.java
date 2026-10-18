package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class CompanionJarPower extends AbstractPower {
    public static final String POWER_ID = "CompanionJarPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CompanionJarPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override public void stackPower(int stackAmount) {
        amount += stackAmount;
        fontScale = 8.0F;
        updateDescription();
    }

    public void onPotionUsed() {
        if (amount <= 0 || !CombatState.isInCombat() || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped() || monster.halfDead) continue;
            // Match Fire Potion: enemy mitigation applies, but Strength/Vigor and attack retaliation do not.
            DamageInfo info = new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS);
            info.applyEnemyPowersOnly(monster);
            addToBot(new DamageAction(monster, info, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
