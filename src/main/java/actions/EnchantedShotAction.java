package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import general.CombatState;
import general.SmithingBody;
import java.util.Set;

public class EnchantedShotAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard sourceCard;
    private final boolean afterAttack;
    private boolean selecting;

    public EnchantedShotAction(AbstractPlayer player, AbstractCard sourceCard) {
        this(player, sourceCard, false);
    }

    private EnchantedShotAction(AbstractPlayer player, AbstractCard sourceCard, boolean afterAttack) {
        this.player = player;
        this.sourceCard = sourceCard;
        this.afterAttack = afterAttack;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (player == null || !CombatState.isInCombat()
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            isDone = true;
            return;
        }
        if (!afterAttack) {
            // Vigor/Pen Nib removal is queued by onUseCard after the card's use().
            addToBot(new EnchantedShotAction(player, sourceCard, true));
            isDone = true;
            return;
        }
        if (!selecting) {
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : player.hand.group) {
                if (eligible(card)) {
                    card.applyPowers();
                    choices.group.add(card);
                }
            }
            if (choices.isEmpty()) isDone = true;
            else if (choices.size() == 1) consume(choices.getTopCard());
            else {
                AbstractDungeon.gridSelectScreen.open(choices, 1, ExhaustAction.TEXT[0], false, false, false, false);
                selecting = true;
            }
            return;
        }
        if (AbstractDungeon.isScreenUp) return;
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            consume(selected);
        } else isDone = true;
    }

    private boolean eligible(AbstractCard card) {
        return card.type == AbstractCard.CardType.ATTACK && card != SmithingBody.physical(sourceCard);
    }

    private void consume(final AbstractCard card) {
        isDone = true;
        if (!eligible(card) || !player.hand.contains(card)) return;
        card.unhover();
        card.applyPowers();
        final int addedDamage = Math.max(0, card.damage);
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                isDone = true;
                if (!player.exhaustPile.contains(card)) return;
                Set<AbstractCard> instances = GetAllInBattleInstances.get(sourceCard.uuid);
                instances.add(sourceCard);
                for (AbstractCard instance : instances) {
                    instance.baseDamage = (int)Math.min(Integer.MAX_VALUE, (long)instance.baseDamage + addedDamage);
                    instance.applyPowers();
                }
            }
        });
        addToTop(new ExhaustSpecificCardAction(card, player.hand));
    }
}
