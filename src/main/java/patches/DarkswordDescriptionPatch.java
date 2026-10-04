package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.CombatState;
import powers.DarkswordPower;

import java.util.IdentityHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class DarkswordDescriptionPatch {
    private static final Map<AbstractCard, String> ORIGINAL_DESCRIPTIONS = new IdentityHashMap<AbstractCard, String>();
    private static final Set<AbstractCard> HIDDEN_CARDS = Collections.newSetFromMap(new WeakHashMap<AbstractCard, Boolean>());
    private static boolean suppressed;

    public static void suppress(boolean value) {
        suppressed = value;
    }

    public static void restoreDescriptions() {
        boolean previous = suppressed;
        suppressed = true;
        try {
            // Include cached previews and rewards, not only cards currently in combat piles.
            for (AbstractCard card : new ArrayList<>(HIDDEN_CARDS)) {
                if (card.rawDescription != null) {
                    card.initializeDescription();
                }
            }
            HIDDEN_CARDS.clear();
        } finally {
            suppressed = previous;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class InitializeDescriptionPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractCard __instance) {
            if (!shouldHideDescription(__instance)) {
                return;
            }
            ORIGINAL_DESCRIPTIONS.put(__instance, __instance.rawDescription);
            HIDDEN_CARDS.add(__instance);
            __instance.rawDescription = hiddenDescription();
        }

        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            if (!ORIGINAL_DESCRIPTIONS.containsKey(__instance)) {
                return;
            }
            __instance.rawDescription = ORIGINAL_DESCRIPTIONS.remove(__instance);
        }
    }

    public static boolean shouldHideDescription(AbstractCard card) {
        return card != null
                && card.rawDescription != null
                && !suppressed
                && AbstractDungeon.player != null
                && CombatState.isInCombat()
                && !CombatState.currentRoom().isBattleOver
                && AbstractDungeon.player.hasPower(DarkswordPower.POWER_ID);
    }

    private static String hiddenDescription() {
        return Settings.language == Settings.GameLanguage.ZHS ? "口口口" : "xxxx";
    }
}
