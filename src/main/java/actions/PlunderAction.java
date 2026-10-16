package actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import patches.BlockedAttackDamagePatch;

public class PlunderAction extends DamageAction {
    private final DamageInfo hit;

    public PlunderAction(AbstractCreature target, DamageInfo info) {
        super(target, info, AttackEffect.SLASH_DIAGONAL);
        this.hit = info;
    }

    @Override
    public void update() {
        if (isDone) return;
        super.update();
        if (isDone) {
            int blocked = BlockedAttackDamagePatch.getBlockedDamage(hit);
            if (blocked > 0) addToTop(new GainBlockAction(hit.owner, hit.owner, blocked));
        }
    }
}
