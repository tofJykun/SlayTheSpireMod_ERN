package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class FragileUsePatch {
    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static final SpireField<Boolean> expires = new SpireField<>(() -> false);
        public static final SpireField<AbstractCard> master = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class Count {
        @SpirePostfixPatch
        public static void postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            FragileField.State s = FragileField.state(card);
            if (s == null || card.dontTriggerOnUseCard) return;
            if (s.remaining > 0) s.remaining--;
            else {
                Fields.expires.set(__instance, true);
                Fields.master.set(__instance, s.master);
            }
            card.initializeDescription();
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class Finish {
        @SpireInsertPatch(locator = BeforeDestination.class)
        public static SpireReturn<Void> insert(UseCardAction __instance, AbstractCard ___targetCard) {
            if (!Fields.expires.get(__instance)) return SpireReturn.Continue();
            Fields.expires.set(__instance, false);
            AbstractCard card = ___targetCard;
            card.freeToPlayOnce = false;
            card.isInAutoplay = false;
            AbstractDungeon.player.cardInUse = null;
            // Deliberate exhaustion: neither Spoon, return-to-hand nor POWER routing may bypass it.
            AbstractDungeon.player.hand.moveToExhaustPile(card);
            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractCard master = Fields.master.get(__instance);
            if (master != null && AbstractDungeon.player.masterDeck.group.contains(master)) {
                AbstractDungeon.player.masterDeck.removeCard(master);
            }
            card.exhaustOnUseOnce = false;
            card.dontTriggerOnUseCard = false;
            __instance.isDone = true;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
            return SpireReturn.Return(null);
        }
    }

    public static class BeforeDestination extends SpireInsertLocator {
        @Override public int[] Locate(CtBehavior behavior) throws Exception {
            return LineFinder.findInOrder(behavior, new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce"));
        }
    }
}
