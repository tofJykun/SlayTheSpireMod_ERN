package actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import java.util.ArrayList;

public class SeaOfMagmaAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] damage;
    private final DamageInfo.DamageType damageType;

    public SeaOfMagmaAction(AbstractPlayer player, int[] damage, DamageInfo.DamageType damageType) {
        this.player = player;
        this.damage = damage.clone();
        this.damageType = damageType;
        actionType = ActionType.EXHAUST;
    }

    @Override
    public void update() {
        if (isDone) return;
        isDone = true;
        CardGroup[] piles = {player.drawPile, player.discardPile, player.hand};
        ArrayList<AbstractCard> statuses = new ArrayList<>();
        for (CardGroup pile : piles) {
            for (AbstractCard card : pile.group) {
                if (card.type == AbstractCard.CardType.STATUS) statuses.add(card);
            }
        }
        int exhausted = 0;
        for (AbstractCard card : statuses) {
            for (CardGroup pile : piles) {
                if (pile.contains(card)) {
                    pile.moveToExhaustPile(card);
                    card.exhaustOnUseOnce = false;
                    card.freeToPlayOnce = false;
                    exhausted++;
                    break;
                }
            }
        }
        if (exhausted > 0) CardCrawlGame.dungeon.checkForPactAchievement();
        // Let exhaust callbacks settle before damage and the final hand-size calculation.
        for (int i = 0; i < exhausted; i++) {
            addToBot(new DamageAllEnemiesAction(player, damage.clone(), damageType, AttackEffect.FIRE));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int missing = Math.max(0, BaseMod.MAX_HAND_SIZE - player.hand.size());
                if (missing > 0) addToTop(new MakeTempCardInHandAction(new Burn(), missing));
                isDone = true;
            }
        });
    }
}
