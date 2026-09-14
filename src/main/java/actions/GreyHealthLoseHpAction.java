package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.LizardTail;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import general.CombatState;
import general.PlayerHpLossHelper;
import potions.ElixirOfLife;
import powers.PowerOfErdtreePower;
import powers.UnyieldingPower;

public class GreyHealthLoseHpAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public GreyHealthLoseHpAction(AbstractCreature target, AbstractCreature source, int amount) {
        setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.NONE;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration == DURATION && this.target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY,
                    this.attackEffect));
        }

        tickDuration();

        if (this.isDone && this.target.currentHealth > 0 && this.amount > 0) {
            try {
                UnyieldingPower.bypassGreyHealth = true;
                loseHpDirectly(this.target, this.amount);
            } finally {
                UnyieldingPower.bypassGreyHealth = false;
            }
            if (!Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
        }
    }

    public static void loseHpDirectly(AbstractCreature target, int amount) {
        if (target == null || target.currentHealth <= 0 || amount <= 0) {
            return;
        }
        if (target.hasPower(PowerOfErdtreePower.POWER_ID)) {
            target.getPower(PowerOfErdtreePower.POWER_ID).flash();
            target.lastDamageTaken = 0;
            return;
        }

        int loss = Math.min(amount, target.currentHealth);
        target.lastDamageTaken = loss;
        GameActionManager.hpLossThisCombat += loss;
        GameActionManager.damageReceivedThisTurn += loss;
        GameActionManager.damageReceivedThisCombat += loss;
        target.currentHealth -= loss;
        target.useStaggerAnimation();
        AbstractDungeon.effectList.add(new StrikeEffect(target, target.hb.cX, target.hb.cY, loss));

        if (target.currentHealth < 0) {
            target.currentHealth = 0;
        } else if (target.currentHealth < target.maxHealth / 4) {
            AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
        }

        target.healthBarUpdatedEvent();

        if (target instanceof AbstractPlayer) {
            PlayerHpLossHelper.onPlayerLostHp((AbstractPlayer)target, loss);
            handlePlayerAfterDirectLoss((AbstractPlayer)target, loss);
        } else if (target.currentHealth < 1) {
            target.isDead = true;
            target.currentHealth = 0;
        }
    }

    private static void handlePlayerAfterDirectLoss(AbstractPlayer player, int loss) {
        if (CombatState.isInCombat()) {
            player.damagedThisCombat++;
        }

        if (player.currentHealth <= player.maxHealth / 2.0F && !player.isBloodied) {
            player.isBloodied = true;
            for (com.megacrit.cardcrawl.relics.AbstractRelic relic : player.relics) {
                if (relic != null) {
                    relic.onBloodied();
                }
            }
        }

        if (player.currentHealth >= 1) {
            return;
        }

        if (!player.hasRelic("Mark of the Bloom")) {
            if (ElixirOfLife.triggerIfPresent()) {
                return;
            }
            if (player.hasPotion("FairyPotion")) {
                for (AbstractPotion potion : player.potions) {
                    if ("FairyPotion".equals(potion.ID)) {
                        potion.flash();
                        player.currentHealth = 0;
                        potion.use(player);
                        AbstractDungeon.topPanel.destroyPotion(potion.slot);
                        return;
                    }
                }
            } else if (player.hasRelic("Lizard Tail")
                    && ((LizardTail)player.getRelic("Lizard Tail")).counter == -1) {
                player.currentHealth = 0;
                player.getRelic("Lizard Tail").onTrigger();
                return;
            }
        }

        player.isDead = true;
        AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        player.currentHealth = 0;
        if (player.currentBlock > 0) {
            player.loseBlock();
        }
    }
}
