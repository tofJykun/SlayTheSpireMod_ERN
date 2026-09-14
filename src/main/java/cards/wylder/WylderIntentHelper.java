package cards.wylder;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.CombatState;

public class WylderIntentHelper {
    private WylderIntentHelper() {}

    public static int incomingDamageBlockGap(AbstractPlayer player) {
        return totalIncomingAttackDamage(player) - player.currentBlock;
    }

    public static int totalIncomingAttackDamage(AbstractPlayer player) {
        AbstractRoom room = CombatState.currentRoom();
        if (room == null || room.monsters == null) {
            return 0;
        }

        int total = 0;
        for (AbstractMonster monster : room.monsters.monsters) {
            if (monster == null || monster.isDeadOrEscaped() || !isAttackIntent(monster.intent)) {
                continue;
            }

            int perHitDamage = monster.getIntentDmg();
            if (perHitDamage <= 0) {
                continue;
            }
            if (player.hasPower("IntangiblePlayer") && perHitDamage > 1) {
                perHitDamage = 1;
            }

            total += perHitDamage * getIntentHits(monster);
        }
        return total;
    }

    public static boolean isPrime(int value) {
        if (value < 2) {
            return false;
        }
        for (int i = 2; i * i <= value; i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int nextPrimeAtLeast(int value) {
        int candidate = Math.max(2, value);
        while (!isPrime(candidate)) {
            candidate++;
        }
        return candidate;
    }

    private static boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF;
    }

    private static int getIntentHits(AbstractMonster monster) {
        int hits = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentMultiAmt");
        return hits > 0 ? hits : 1;
    }
}
