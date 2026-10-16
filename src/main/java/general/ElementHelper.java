package general;

import actions.ElementCopyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.ElementPatch.Fields;

public final class ElementHelper {
    private static CardElement last;
    private static boolean active;

    private ElementHelper() {}

    public static boolean isElementCopy(AbstractCard card) { return Fields.elementCopy.get(card); }

    public static void startCombat() { last = null; active = true; BrandNewWorldProgress.resetCombat(); }
    public static void endCombat() { last = null; active = false; BrandNewWorldProgress.resetCombat(); refreshHand(); }

    public static CardElement element(AbstractCard card) {
        if (card == null) return null;
        CardElement explicit = Fields.element.get(card);
        if (explicit != null) return explicit;
        String text = card.rawDescription;
        if (text != null) {
            for (CardElement value : CardElement.values()) {
                if (text.contains(value.chinese) || text.contains(value.english)
                        || text.contains(value.english.replace(' ', '_'))) return value;
            }
        }
        return null;
    }

    public static int relation(AbstractCard card) {
        CardElement current = element(card);
        if (!active || current == null || last == null) return 0;
        if (last == current.forwardPredecessor()) return 1;
        if (last == current.reversePredecessor()) return -1;
        return 0;
    }

    public static void removeSurcharge(AbstractCard card) {
        if (!Fields.surcharge.get(card)) return;
        // A direct cost assignment by another effect replaces the old temporary cost.
        if (card.costForTurn == Fields.displayedCost.get(card)) card.costForTurn--;
        Fields.surcharge.set(card, false);
        card.isCostModifiedForTurn = Fields.originalModified.get(card);
    }

    public static void refreshCost(AbstractCard card) {
        if (Fields.resolving.get(card)) return;
        removeSurcharge(card);
        if (active && AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(card)
                && relation(card) == -1 && card.costForTurn >= 0 && card.cost >= 0) {
            Fields.originalModified.set(card, card.isCostModifiedForTurn);
            card.costForTurn++;
            card.isCostModifiedForTurn = true;
            Fields.displayedCost.set(card, card.costForTurn);
            Fields.surcharge.set(card, true);
        }
    }

    public static void refreshHand() {
        if (AbstractDungeon.player == null) return;
        for (AbstractCard card : AbstractDungeon.player.hand.group) refreshCost(card);
    }

    public static void beginUse(AbstractCard card) {
        refreshCost(card);
        Fields.resolving.set(card, true);
        Fields.pendingCopy.set(card, null);
    }

    public static void beforeEffect(AbstractCard card) {
        if (!active || card.dontTriggerOnUseCard) return;
        if (relation(card) == 1 && !Fields.elementCopy.get(card)) {
            // Capture form and computed numbers before use() can change either of them.
            AbstractCard copy = SmithingBody.copy(card);
            copy.damage = card.damage;
            copy.block = card.block;
            copy.magicNumber = card.magicNumber;
            copy.multiDamage = card.multiDamage == null ? null : card.multiDamage.clone();
            copy.damageTypeForTurn = card.damageTypeForTurn;
            Fields.elementCopy.set(copy, true);
            Fields.pendingCopy.set(card, copy);
            BrandNewWorldProgress.onForward(element(card));
        }
        last = element(card); // A non-element card also becomes the last card.
        refreshHand();
    }

    public static void finishUse(AbstractCard card, AbstractMonster target) {
        AbstractCard copy = Fields.pendingCopy.get(card);
        Fields.pendingCopy.set(card, null);
        Fields.resolving.set(card, false);
        refreshCost(card);
        refreshHand();
        if (copy != null) {
            copy.energyOnUse = card.energyOnUse;
            AbstractDungeon.actionManager.addToBottom(new ElementCopyAction(copy, target));
        }
    }

    public static void inherit(AbstractCard copy, AbstractCard source) {
        Fields.element.set(copy, Fields.element.get(source));
        Fields.elementCopy.set(copy, Fields.elementCopy.get(source));
        if (Fields.surcharge.get(source) && copy.costForTurn == Fields.displayedCost.get(source)) {
            copy.costForTurn--;
            copy.isCostModifiedForTurn = Fields.originalModified.get(source);
        }
    }
}
