package patches;

import actions.ShadowGuardCopyAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingBody;

public final class ShadowGuardField {
    private ShadowGuardField() {}

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> marked = new SpireField<>(() -> false);
        public static final SpireField<Boolean> shadowCopy = new SpireField<>(() -> false);
        public static final SpireField<AbstractCard> pendingCopy = new SpireField<>(() -> null);
    }

    public static boolean isMarked(AbstractCard card) {
        return card != null && Fields.marked.get(card);
    }

    public static void mark(AbstractCard card) {
        Fields.marked.set(card, true);
        card.initializeDescription();
    }

    public static void inherit(AbstractCard destination, AbstractCard source) {
        if (source == null || destination == null) return;
        Fields.marked.set(destination, isMarked(source));
        Fields.shadowCopy.set(destination, Fields.shadowCopy.get(source));
        if (isMarked(destination)) destination.initializeDescription();
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyPatch {
        @SpirePostfixPatch
        public static AbstractCard postfix(AbstractCard __result, AbstractCard __instance) {
            inherit(__result, __instance);
            return __result;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class PlayPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, AbstractCard card, AbstractMonster target, int energyOnUse) {
            Fields.pendingCopy.set(card, null);
            // Duplicate a full replay chain once, not once per link (Replay 2 => 3 + 3 plays).
            if (!isMarked(card) || Fields.shadowCopy.get(card) || ReplayField.isReplayCopy(card)
                    || card.dontTriggerOnUseCard) return;
            AbstractCard snapshot = SmithingBody.copy(card);
            Fields.shadowCopy.set(snapshot, true);
            Fields.pendingCopy.set(card, snapshot);
        }

        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, AbstractCard card, AbstractMonster target, int energyOnUse) {
            AbstractCard snapshot = Fields.pendingCopy.get(card);
            Fields.pendingCopy.set(card, null);
            if (snapshot != null && AbstractDungeon.actionManager != null) {
                snapshot.energyOnUse = card.energyOnUse;
                AbstractDungeon.actionManager.addToBottom(new ShadowGuardCopyAction(snapshot, target));
            }
        }
    }
}
