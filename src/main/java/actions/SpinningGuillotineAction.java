package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpinningGuillotineAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractCard source;

    public SpinningGuillotineAction(AbstractPlayer player, AbstractMonster target, AbstractCard source) {
        this.player = player;
        this.source = source;
        setValues(target, player);
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        isDone = true;
        if (player == null || source == null || target == null) {
            return;
        }
        if (target.isDying || target.currentHealth <= 0 || target.isDeadOrEscaped() || target.halfDead) {
            addToTop(new MakeTempCardInHandAction(source.makeStatEquivalentCopy(), 1));
        }
    }
}
