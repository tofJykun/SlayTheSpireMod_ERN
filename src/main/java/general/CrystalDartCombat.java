package general;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.CrystalDartPower;
import java.util.ArrayList;

public final class CrystalDartCombat {
    private CrystalDartCombat() { }

    public static boolean living(AbstractMonster monster) {
        return monster != null && !monster.isDeadOrEscaped() && !monster.isDying
                && !monster.halfDead && monster.currentHealth > 0;
    }

    private static ArrayList<AbstractMonster> others(AbstractMonster source) {
        ArrayList<AbstractMonster> result = new ArrayList<>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster != source && living(monster)) result.add(monster);
            }
        }
        return result;
    }

    public static AbstractMonster randomOther(AbstractMonster source) {
        ArrayList<AbstractMonster> candidates = others(source);
        // Same queue and inclusive range as DamageRandomEnemyAction / Sword Boomerang.
        return candidates.isEmpty() ? null
                : candidates.get(AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1));
    }

    public static void apply(AbstractMonster monster, AbstractCreature source) {
        if (!living(monster) || others(monster).isEmpty() || monster.hasPower(CrystalDartPower.POWER_ID)) return;
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(monster, source,
                new CrystalDartPower(monster)));
    }

    public static AbstractMonster target(AbstractCreature original, DamageInfo info) {
        if (original != AbstractDungeon.player || info == null || info.type != DamageInfo.DamageType.NORMAL
                || !(info.owner instanceof AbstractMonster) || !living((AbstractMonster)info.owner)) return null;
        AbstractPower power = info.owner.getPower(CrystalDartPower.POWER_ID);
        return power instanceof CrystalDartPower ? ((CrystalDartPower)power).target() : null;
    }

    public static DamageInfo damageFor(DamageInfo original, AbstractMonster target) {
        DamageInfo result = new DamageInfo(original.owner, original.base, original.type);
        // Enemy DamageInfo normally includes the player's stance. Recompute for the new
        // defender without that player-only step, and never mutate the monster's shared info.
        float value = result.base;
        if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
            value *= AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
        }
        for (AbstractPower power : result.owner.powers) value = power.atDamageGive(value, result.type);
        for (AbstractPower power : target.powers) value = power.atDamageReceive(value, result.type);
        for (AbstractPower power : result.owner.powers) value = power.atDamageFinalGive(value, result.type);
        for (AbstractPower power : target.powers) value = power.atDamageFinalReceive(value, result.type);
        result.output = Math.max(0, (int)Math.floor(value));
        result.isModified = result.output != result.base;
        return result;
    }
}
