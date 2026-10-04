package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.SmithingBody;

public class FallingstarBeastJawAction extends AbstractGameAction {
    private final AbstractCard card;
    private final AbstractMonster monster;
    private final DamageInfo info;

    public FallingstarBeastJawAction(AbstractCard card, AbstractMonster monster, DamageInfo info) {
        this.card = SmithingBody.physical(card);
        this.monster = monster;
        this.info = info;
        actionType = ActionType.DAMAGE;
    }

    private static boolean alive(AbstractMonster monster) {
        return monster != null && !monster.isDeadOrEscaped() && !monster.isDying
                && !monster.halfDead && monster.currentHealth > 0;
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        if (!alive(monster)) return;
        // Equality removes the last Block but does not deal unblocked damage.
        boolean repeat = monster.currentBlock > 0 && info.output <= monster.currentBlock;
        addToTop(new DamageAction(monster, info, AttackEffect.BLUNT_HEAVY));
        if (repeat) {
            // Append after UseCardAction: it must finish moving this physical card first.
            addToBot(new RequeueAction(card, monster));
        }
    }

    private static class RequeueAction extends AbstractGameAction {
        private final AbstractCard card;
        private final AbstractMonster monster;

        private RequeueAction(AbstractCard card, AbstractMonster monster) {
            this.card = card;
            this.monster = monster;
        }

        @Override
        public void update() {
            if (isDone) return;
            isDone = true;
            AbstractPlayer p = AbstractDungeon.player;
            if (p == null || p.isDying || p.currentHealth <= 0 || !alive(monster)
                    || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT
                    || AbstractDungeon.actionManager.turnHasEnded
                    || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= 999) return;
            for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
                if (item.card == card) return;
            }

            boolean found = false;
            for (CardGroup pile : new CardGroup[] {p.hand, p.drawPile, p.discardPile, p.exhaustPile, p.limbo}) {
                while (pile.group.remove(card)) found = true;
            }
            if (!found && !card.purgeOnUse) return;
            AbstractDungeon.getCurrRoom().souls.remove(card);
            card.unhover();
            card.untip();
            card.stopGlowing();
            card.unfadeOut();
            card.lighten(false);
            card.target_x = p.hb.cX;
            card.target_y = p.hb.cY;
            card.targetAngle = 0.0F;
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.freeToPlayOnce = true;
            card.applyPowers();
            p.limbo.addToBottom(card);
            p.hand.refreshHandLayout();
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, monster, 0, true, true), true);
        }
    }
}
