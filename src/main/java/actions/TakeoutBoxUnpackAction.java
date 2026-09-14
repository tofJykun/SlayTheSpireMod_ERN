package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.PackagingCard;

import java.util.ArrayList;

public class TakeoutBoxUnpackAction extends AbstractGameAction {
    private final AbstractCard boxCard;
    private final PackagingCard box;
    private final boolean clearAfterReturn;

    public TakeoutBoxUnpackAction(AbstractCard boxCard, PackagingCard box) {
        this(boxCard, box, true);
    }

    public TakeoutBoxUnpackAction(AbstractCard boxCard, PackagingCard box, boolean clearAfterReturn) {
        this.boxCard = boxCard;
        this.box = box;
        this.clearAfterReturn = clearAfterReturn;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.box == null || this.box.getPackagedCards().isEmpty()) {
            this.isDone = true;
            return;
        }

        ArrayList<AbstractCard> originals = new ArrayList<AbstractCard>(this.box.getPackagedCards());
        for (AbstractCard original : originals) {
            queueCopy(original);
        }
        addToBot(new TakeoutBoxReturnCardsAction(this.box, originals, this.clearAfterReturn));
        this.isDone = true;
    }

    private void queueCopy(AbstractCard original) {
        AbstractCard copy = original.makeStatEquivalentCopy();
        copy.current_x = this.boxCard.current_x;
        copy.current_y = this.boxCard.current_y;
        copy.target_x = Settings.WIDTH / 2.0F;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.targetAngle = 0.0F;
        copy.lighten(false);
        copy.drawScale = 0.12F;
        copy.targetDrawScale = 0.75F;
        copy.freeToPlayOnce = true;
        copy.purgeOnUse = true;
        copy.applyPowers();
        AbstractDungeon.player.limbo.addToBottom(copy);
        if (copy.target == AbstractCard.CardTarget.ENEMY || copy.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, true,
                    EnergyPanel.getCurrentEnergy(), true, true), false);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, null,
                    EnergyPanel.getCurrentEnergy(), true, true), false);
        }
    }
}
