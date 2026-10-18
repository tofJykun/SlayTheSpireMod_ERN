package actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.RelicRewardHelper;

public class LordOfLordsAction extends DamageAction {
    private boolean rewarded;

    public LordOfLordsAction(AbstractMonster target, DamageInfo info) {
        super(target, info, AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void update() {
        if (rewarded) return;
        super.update();
        // Keep the reward in the damage action: victory clears ordinary queued actions.
        if (isDone) {
            rewarded = true;
            if (AbstractDungeon.player != null && !AbstractDungeon.player.isDead
                    && !AbstractDungeon.player.isDying) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F,
                        Settings.HEIGHT / 2.0F, RelicRewardHelper.returnRandomEliteDropRelic());
            }
        }
    }
}
