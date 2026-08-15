package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class GlintbladePhalanxAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int damage;
    private final int block;
    private final DamageInfo.DamageType damageType;
    private final boolean freeToPlayOnce;
    private final int energyOnUse;

    public GlintbladePhalanxAction(AbstractPlayer player, AbstractMonster target, int damage, int block,
                                   DamageInfo.DamageType damageType, boolean freeToPlayOnce, int energyOnUse) {
        this.player = player;
        this.target = target;
        this.damage = damage;
        this.block = block;
        this.damageType = damageType;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.player.hasRelic("Chemical X")) {
            effect += 2;
            this.player.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                addToBot((AbstractGameAction)new DamageAction((AbstractCreature)this.target,
                        new DamageInfo((AbstractCreature)this.player, this.damage, this.damageType),
                        AttackEffect.SLASH_DIAGONAL));
                addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)this.player,
                        (AbstractCreature)this.player, this.block));
            }
            if (!this.freeToPlayOnce) {
                this.player.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}
