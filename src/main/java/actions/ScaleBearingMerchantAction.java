package actions;

import cards.revenant.LotOfRunes;
import cards.revenant.PowerfulWeapon;
import cards.revenant.ResistanceToAilments;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class ScaleBearingMerchantAction extends AbstractGameAction {
    private final boolean upgraded;
    private boolean opened;

    public ScaleBearingMerchantAction(boolean upgraded) {
        this.upgraded = upgraded;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.isDone) return;
        if (!this.opened) {
            this.opened = true;
            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(makeChoice(new PowerfulWeapon()));
            choices.add(makeChoice(new ResistanceToAilments()));
            choices.add(makeChoice(new LotOfRunes()));
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], false);
            return;
        }
        if (AbstractDungeon.isScreenUp) return;
        AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard;
        if (selected == null) return;
        AbstractDungeon.cardRewardScreen.discoveryCard = null;
        this.isDone = true;
        selected.use(AbstractDungeon.player, null);
    }

    private AbstractCard makeChoice(AbstractCard card) {
        if (this.upgraded && card.canUpgrade()) {
            card.upgrade();
        }
        return card;
    }
}
