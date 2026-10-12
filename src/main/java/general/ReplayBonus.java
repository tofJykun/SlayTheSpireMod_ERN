package general;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.ReplayField;

public final class ReplayBonus {
    private ReplayBonus() {}

    public static int effective(AbstractCard card) {
        int base = ReplayField.getReplay(card);
        if (base <= 0 || ReplayField.isReplayCopy(card) || AbstractDungeon.player == null) return base;
        long bonus = AbstractDungeon.player.hasRelic("ProstheticHand") ? 1 : 0;
        if (CombatState.isInCombat() && !CombatState.currentRoom().isBattleOver) {
            AbstractPower power = AbstractDungeon.player.getPower("EdgeStatePower");
            if (power != null) bonus += Math.max(0, power.amount);
        }
        return (int)Math.min(Integer.MAX_VALUE, (long)base + bonus);
    }

    public static void refreshDescriptions() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) return;
        for (CardGroup group : new CardGroup[] {player.hand, player.drawPile, player.discardPile,
                player.exhaustPile, player.limbo}) {
            for (AbstractCard card : group.group) {
                if (ReplayField.getReplay(card) > 0) card.initializeDescription();
            }
        }
    }
}
