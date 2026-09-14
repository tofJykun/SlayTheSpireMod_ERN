package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.UUID;

public class HandAxeReturnAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final ArrayList<UUID> cardUuids;

    public HandAxeReturnAction(ArrayList<UUID> cardUuids) {
        this.player = AbstractDungeon.player;
        this.cardUuids = new ArrayList<>(cardUuids);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.player == null) {
            this.isDone = true;
            return;
        }

        for (UUID uuid : this.cardUuids) {
            if (this.player.hand.size() >= 10) {
                this.player.createHandIsFullDialog();
                break;
            }
            if (!moveFrom(this.player.drawPile, uuid)) {
                moveFrom(this.player.discardPile, uuid);
            }
        }

        this.player.hand.refreshHandLayout();
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
        this.isDone = true;
    }

    private boolean moveFrom(CardGroup group, UUID uuid) {
        if (group == null || uuid == null) {
            return false;
        }
        for (AbstractCard card : group.group) {
            if (uuid.equals(card.uuid)) {
                card.unhover();
                card.unfadeOut();
                card.fadingOut = false;
                group.removeCard(card);
                this.player.hand.addToHand(card);
                return true;
            }
        }
        return false;
    }
}
