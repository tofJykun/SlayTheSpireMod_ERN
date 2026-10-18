package actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import general.CombatState;
import patches.SigilOfBaphometPatch;

public class SigilOfBaphometAction extends AbstractGameAction {
    private final String speech;

    public SigilOfBaphometAction(String speech) { this.speech = speech; }

    @Override
    public void update() {
        isDone = true;
        AbstractRoom room = CombatState.currentRoom();
        if (!CombatState.isInCombat() || room.isBattleOver || room.monsters == null) return;
        for (AbstractMonster monster : room.monsters.monsters) {
            if (!(monster instanceof Cultist || monster instanceof Chosen || monster instanceof AwakenedOne)
                    || monster.isEscaping || (monster.isDeadOrEscaped() && !monster.halfDead)) continue;
            if (monster instanceof AwakenedOne) {
                // Rebirth must not keep the encounter locked after the boss leaves.
                monster.halfDead = false;
                room.cannotLose = false;
                if (room instanceof MonsterRoomBoss) {
                    ReflectionHacks.privateMethod(AbstractMonster.class, "onBossVictoryLogic").invoke(monster);
                    ReflectionHacks.privateMethod(AbstractMonster.class, "onFinalBossVictoryLogic").invoke(monster);
                }
            }
            AbstractDungeon.effectList.add(new SpeechBubble(monster.hb.cX + monster.dialogX,
                    monster.hb.cY + monster.dialogY, 3.0F, speech, false));
            // Native escape animation ends the battle once all remaining enemies have left/died.
            SigilOfBaphometPatch.Banished.bySigil.set(monster, true);
            monster.escape();
        }
    }
}
