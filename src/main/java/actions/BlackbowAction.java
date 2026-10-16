package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.CombatState;
import general.SmithingBody;
import patches.TripleBurrowsPatch;
import java.util.ArrayList;

public class BlackbowAction extends AbstractGameAction {
    private final ArrayList<AbstractCard> cards;

    public BlackbowAction(ArrayList<AbstractCard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        if (!CombatState.isInCombat() || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        for (int i = 0; i < cards.size(); i++) {
            AbstractCard card = SmithingBody.physical(cards.get(i));
            // Let the native queue check canUse at resolution, after earlier cards have paid their costs.
            // A historical forced replay must not transfer its condition-bypass flags to Blackbow.
            card.dontTriggerOnUseCard = false;
            TripleBurrowsPatch.Fields.forcedCopy.set(card, false);
            AbstractCard body = SmithingBody.behavior(card);
            body.dontTriggerOnUseCard = false;
            TripleBurrowsPatch.Fields.forcedCopy.set(body, false);
            card.freeToPlayOnce = true;
            card.purgeOnUse = true;
            card.current_x = card.target_x = AbstractDungeon.player.hb.cX;
            card.current_y = card.target_y = AbstractDungeon.player.hb.cY;
            card.targetAngle = 0.0F;
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.applyPowers();
            AbstractDungeon.player.limbo.addToBottom(card);
            boolean needsTarget = card.target == AbstractCard.CardTarget.ENEMY
                    || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
            CardQueueItem item = needsTarget
                    ? new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), true, true)
                    : new CardQueueItem(card, null, EnergyPanel.getCurrentEnergy(), true, true);
            AbstractDungeon.actionManager.addCardQueueItem(item, false);
        }
    }
}
