package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class BlinkboltAction extends AbstractGameAction {
    private final AbstractCard card;
    private final int hits;
    private final int drawPerEnemy;

    public BlinkboltAction(AbstractCard card, int hits, int drawPerEnemy) {
        this.card = card;
        this.hits = hits;
        this.drawPerEnemy = drawPerEnemy;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        final Set<AbstractCreature> hitEnemies = Collections.newSetFromMap(
                new IdentityHashMap<AbstractCreature, Boolean>());
        // Keep all hits ahead of card cleanup, and draw only after the last hit resolves.
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (!hitEnemies.isEmpty()) {
                    addToTop(new DrawCardAction(hitEnemies.size() * drawPerEnemy));
                }
                this.isDone = true;
            }
        });
        for (int i = 0; i < this.hits; i++) {
            addToTop(new AttackDamageRandomEnemyAction(this.card, AttackEffect.SLASH_HORIZONTAL) {
                @Override
                public void update() {
                    super.update();
                    if (this.target != null) {
                        hitEnemies.add(this.target);
                    }
                }
            });
        }
    }
}
