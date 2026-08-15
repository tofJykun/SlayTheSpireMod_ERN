package general;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class DeadEnemyStats {
    private static final Set<AbstractMonster> recordedMonsters =
            Collections.newSetFromMap(new IdentityHashMap<AbstractMonster, Boolean>());
    private static int deadEnemyMaxHealthSum;

    private DeadEnemyStats() {
    }

    public static void resetCombat() {
        recordedMonsters.clear();
        deadEnemyMaxHealthSum = 0;
    }

    public static void recordMonsterDeath(AbstractMonster monster) {
        if (monster == null || recordedMonsters.contains(monster)) {
            return;
        }
        recordedMonsters.add(monster);
        deadEnemyMaxHealthSum += Math.max(0, monster.maxHealth);
    }

    public static int getDeadEnemyMaxHealthSum() {
        return deadEnemyMaxHealthSum;
    }
}
