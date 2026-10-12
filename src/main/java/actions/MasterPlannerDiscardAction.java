package actions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.ScheduledField;
import powers.MasterPlannerPower;
import java.util.ArrayList;

/** Used only by the automatic end-turn discard, leaving all end-turn card hooks intact. */
public class MasterPlannerDiscardAction extends DiscardAction {
    private final AbstractPlayer player;

    public MasterPlannerDiscardAction(AbstractCreature target, AbstractCreature source, int amount,
                                      boolean random, boolean endTurn) {
        super(target, source, amount, random, endTurn);
        player = (AbstractPlayer)target;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (!player.hasPower(MasterPlannerPower.POWER_ID)) {
            super.update();
            return;
        }
        isDone = true;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        for (AbstractCard card : new ArrayList<>(player.hand.group)) {
            if (!ScheduledField.isScheduled(card) && player.hand.contains(card)) {
                player.hand.moveToDiscardPile(card);
                GameActionManager.incrementDiscard(true);
            }
        }
        player.hand.applyPowers();
    }
}
