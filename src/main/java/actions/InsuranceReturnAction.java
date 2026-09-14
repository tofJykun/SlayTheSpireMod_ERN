package actions;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.Iterator;
import java.util.List;

public class InsuranceReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard card;

    public InsuranceReturnAction(AbstractPlayer player, AbstractCard card) {
        this.player = player;
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        this.isDone = true;
        if (this.player == null || this.card == null || !this.player.exhaustPile.contains(this.card)) {
            return;
        }
        // Match Exhume: leave the original in the exhaust pile if the hand is full.
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            this.player.createHandIsFullDialog();
            return;
        }
        // An old exhaust effect must not render or reset the recovered original later.
        cancelExhaustEffects(AbstractDungeon.effectList);
        cancelExhaustEffects(AbstractDungeon.effectsQueue);
        cancelExhaustEffects(AbstractDungeon.topLevelEffects);
        cancelExhaustEffects(AbstractDungeon.topLevelEffectsQueue);
        this.card.unfadeOut();
        // moveToHand also clears the opaque tint left by discard/shuffle animations.
        this.player.hand.moveToHand(this.card, this.player.exhaustPile);
        if (this.player.hasPower("Corruption") && this.card.type == AbstractCard.CardType.SKILL) {
            this.card.setCostForTurn(-9);
        }
        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
    }

    private void cancelExhaustEffects(List<AbstractGameEffect> effects) {
        for (Iterator<AbstractGameEffect> iterator = effects.iterator(); iterator.hasNext();) {
            AbstractGameEffect effect = iterator.next();
            if (effect instanceof ExhaustCardEffect
                    && ReflectionHacks.getPrivate(effect, ExhaustCardEffect.class, "c") == this.card) {
                effect.isDone = true;
                effect.dispose();
                iterator.remove();
            }
        }
    }
}
