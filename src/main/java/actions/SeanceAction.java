package actions;

import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class SeanceAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int[] multiDamage;
    private final DamageInfo.DamageType damageType;

    public SeanceAction(AbstractPlayer player, int[] multiDamage, DamageInfo.DamageType damageType) {
        this.player = player;
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>();
        for (AbstractCard card : this.player.hand.group) {
            if (card instanceof AbstractPhantomCard) {
                cardsToExhaust.add(card);
            }
        }

        for (AbstractCard card : cardsToExhaust) {
            addToTop((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)this.player,
                    this.multiDamage, this.damageType, AttackEffect.FIRE));
            addToTop((AbstractGameAction)new ExhaustSpecificCardAction(card, this.player.hand));
        }
        this.isDone = true;
    }
}
