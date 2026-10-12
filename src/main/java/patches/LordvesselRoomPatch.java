package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import general.LordvesselHistory;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class LordvesselRoomPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "generateSeeds")
    public static class NewRun {
        @SpirePrefixPatch
        public static void prefix() { LordvesselHistory.reset(); }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class Transition {
        @SpirePrefixPatch
        public static void prefix() { LordvesselHistory.prepare(); }

        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals("java.util.ArrayList") && call.getMethodName().equals("remove")
                            && call.getSignature().equals("(I)Ljava/lang/Object;")) {
                        call.replace("{ $_ = general.LordvesselHistory.removeEncounter($0, $1); }");
                    } else if (call.getClassName().equals(EventHelper.class.getName()) && call.getMethodName().equals("roll")) {
                        call.replace("{ $_ = general.LordvesselHistory.rollRoom($1); }");
                    } else if (call.getMethodName().equals("onPlayerEntry")) {
                        call.replace("{ general.LordvesselHistory.enter($0); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "getEvent", paramtypez = {String.class})
    public static class EventId {
        @SpirePrefixPatch
        public static void prefix(String key) { LordvesselHistory.rememberEvent(key); }
    }

    @SpirePatches({
            @SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry"),
            @SpirePatch(clz = MonsterRoomElite.class, method = "onPlayerEntry"),
            @SpirePatch(clz = MonsterRoomBoss.class, method = "onPlayerEntry"),
            @SpirePatch(clz = EventRoom.class, method = "onPlayerEntry"),
            @SpirePatch(clz = EventRoom.class, method = "update")})
    public static class RoomEntry {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall call) throws CannotCompileException {
                    String method = call.getMethodName();
                    if (call.getClassName().equals(AbstractDungeon.class.getName())) {
                        if (method.equals("getMonsterForRoomCreation")) call.replace("{ $_ = general.LordvesselHistory.encounter(false); }");
                        if (method.equals("getEliteMonsterForRoomCreation")) call.replace("{ $_ = general.LordvesselHistory.encounter(true); }");
                        if (method.equals("getBoss")) call.replace("{ $_ = general.LordvesselHistory.boss(); }");
                        if (method.equals("generateEvent")) call.replace("{ $_ = general.LordvesselHistory.generateEvent($1); }");
                    } else if (call.getClassName().equals(AbstractEvent.class.getName())) {
                        if (method.equals("update")) call.replace("{ general.LordvesselHistory.updateEvent($0, false); }");
                        if (method.equals("onEnterRoom")) call.replace("{ general.LordvesselHistory.updateEvent($0, true); }");
                    } else if (call.getClassName().equals("java.util.ArrayList") && method.equals("remove")
                            && call.getSignature().equals("(I)Ljava/lang/Object;")) {
                        call.replace("{ $_ = general.LordvesselHistory.removeEncounter($0, $1); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = Random.class, method = "randomLong")
    public static class EventRandom {
        @SpireRawPatch
        public static void raw(CtBehavior target) throws Exception {
            for (CtMethod method : target.getDeclaringClass().getDeclaredMethods()) {
                String name = method.getName();
                if (!name.equals("random") && !name.equals("randomBoolean") && !name.equals("randomLong")) continue;
                String type = method.getReturnType().getName();
                String parser = type.equals("int") ? "Integer.parseInt" : type.equals("long") ? "Long.parseLong"
                        : type.equals("float") ? "Float.parseFloat" : "Boolean.parseBoolean";
                method.addLocalVariable("ernRoomRandomKey", target.getDeclaringClass().getClassPool().get("java.lang.String"));
                method.addLocalVariable("ernRoomRandomCached", CtClass.booleanType);
                method.addLocalVariable("ernRoomRandomSaved", target.getDeclaringClass().getClassPool().get("java.lang.String"));
                method.insertBefore("{ ernRoomRandomCached = false; ernRoomRandomKey = general.LordvesselHistory.randomKey(\"" + name + method.getSignature()
                        + "\" + java.util.Arrays.toString($args)); ernRoomRandomSaved = general.LordvesselHistory.randomValue(ernRoomRandomKey);"
                        + " if (ernRoomRandomSaved != null) { ernRoomRandomCached = true; return " + parser + "(ernRoomRandomSaved); } }");
                method.insertAfter("{ if (!ernRoomRandomCached) general.LordvesselHistory.rememberRandom(ernRoomRandomKey, String.valueOf($_)); }");
            }
        }
    }

    @SpirePatches({
            @SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards"),
            @SpirePatch(clz = AbstractDungeon.class, method = "getColorlessRewardCards")})
    public static class EventCards {
        @SpireRawPatch
        public static void raw(CtBehavior target) throws Exception {
            ClassPool pool = target.getDeclaringClass().getClassPool();
            target.addLocalVariable("ernRoomCardsKey", pool.get("java.lang.String"));
            target.addLocalVariable("ernRoomCardsCached", CtClass.booleanType);
            target.addLocalVariable("ernRoomCardsSaved", pool.get("java.util.ArrayList"));
            target.insertBefore("{ ernRoomCardsCached = false; ernRoomCardsKey = general.LordvesselHistory.cardsKey(\"" + target.getName()
                    + "\"); ernRoomCardsSaved = general.LordvesselHistory.savedCards(ernRoomCardsKey);"
                    + " if (ernRoomCardsSaved != null) { ernRoomCardsCached = true; return ernRoomCardsSaved; } }");
            target.insertAfter("{ if (!ernRoomCardsCached) general.LordvesselHistory.rememberCards(ernRoomCardsKey, $_); }");
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomCard")
    public static class FixedContent {
        @SpireRawPatch
        public static void raw(CtBehavior target) throws Exception {
            ClassPool pool = target.getDeclaringClass().getClassPool();
            for (CtMethod method : target.getDeclaringClass().getDeclaredMethods()) {
                String result = method.getReturnType().getName();
                String kind = result.equals("com.megacrit.cardcrawl.cards.AbstractCard") ? "card"
                        : result.equals("com.megacrit.cardcrawl.relics.AbstractRelic") ? "relic"
                        : result.equals("com.megacrit.cardcrawl.potions.AbstractPotion") ? "potion" : null;
                if (kind == null || !Modifier.isStatic(method.getModifiers())) continue;
                method.addLocalVariable("ernContentKey", pool.get("java.lang.String"));
                method.addLocalVariable("ernContentCached", CtClass.booleanType);
                method.addLocalVariable("ernContentSaved", pool.get("java.lang.Object"));
                method.insertBefore("{ ernContentCached = false; ernContentKey = general.LordvesselHistory.contentKey(\""
                        + method.getName() + method.getSignature() + "\"); ernContentSaved = general.LordvesselHistory.savedContent(ernContentKey, \""
                        + kind + "\"); if (ernContentSaved != null) { ernContentCached = true; return (" + result + ")ernContentSaved; } }");
                method.insertAfter("{ if (!ernContentCached) general.LordvesselHistory.rememberContent(ernContentKey, $_); }");
            }
        }
    }

    @SpirePatch(clz = AbstractChest.class, method = "open", paramtypez = {boolean.class})
    public static class ChestContent {
        @SpireRawPatch
        public static void raw(CtBehavior target) throws Exception {
            target.insertBefore("{ general.LordvesselHistory.beginChest(); }");
            target.insertAfter("{ general.LordvesselHistory.endChest(); }", true);
        }
    }
}
