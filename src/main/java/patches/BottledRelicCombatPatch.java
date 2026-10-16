package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public final class BottledRelicCombatPatch {
    @SpirePatch(clz = BottledFlame.class, method = "onEquip")
    @SpirePatch(clz = BottledFlame.class, method = "update")
    @SpirePatch(clz = BottledLightning.class, method = "onEquip")
    @SpirePatch(clz = BottledLightning.class, method = "update")
    @SpirePatch(clz = BottledTornado.class, method = "onEquip")
    @SpirePatch(clz = BottledTornado.class, method = "update")
    public static class PreserveCombatPhase {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isWriter() && field.getClassName().equals(AbstractRoom.class.getName())
                            && field.getFieldName().equals("phase")) {
                        // Keep COMBAT even while opening the grid: its UI also depends on the room phase.
                        field.replace("{ if ($0.phase != com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT)"
                                + " { $proceed($$); } }");
                    }
                }
            };
        }
    }
}
