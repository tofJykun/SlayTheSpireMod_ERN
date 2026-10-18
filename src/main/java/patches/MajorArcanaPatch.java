package patches;

import cards.scholar.MajorArcana;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import general.MajorArcanaCycle;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import powers.MajorArcanaTemperancePower;

public final class MajorArcanaPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class Play {
        @SpirePostfixPatch public static void postfix(AbstractPlayer __instance, AbstractCard card, AbstractMonster monster, int energy) {
            MajorArcanaCycle.afterPlay();
            for (AbstractCard c : __instance.hand.group) if (c instanceof MajorArcana) c.applyPowers();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription")
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN")
    public static class Popup {
        @SpirePrefixPatch public static void prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof MajorArcana) ___card.applyPowers();
        }
    }

    public static int reduce(AbstractPlayer player, DamageInfo info, int damage) {
        AbstractPower power = player.getPower(MajorArcanaTemperancePower.POWER_ID);
        return power instanceof MajorArcanaTemperancePower
                ? ((MajorArcanaTemperancePower)power).reduce(damage, info.type) : damage;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class Temperance {
        @SpireInstrumentPatch public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && DamageInfo.class.getName().equals(field.getClassName()) && "output".equals(field.getFieldName())) {
                        field.replace("$_ = patches.MajorArcanaPatch.reduce(this, $0, $proceed());");
                    }
                }
            };
        }
    }
}
