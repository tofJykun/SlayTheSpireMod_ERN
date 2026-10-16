package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.screens.DeathScreen;
import powers.OriginalSinAttackOnlyPower;
import powers.OriginalSinNoAttackPower;

import java.util.ArrayList;

public class OriginalSinAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int stage;
    private final int draw;

    public OriginalSinAction(AbstractPlayer player, int stage, int draw) {
        this.player = player;
        this.stage = stage;
        this.draw = draw;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (this.isDone) return;
        this.isDone = true;
        if (player == null || player.isDead) return;
        if (stage == 0) {
            addToTop(new AbstractGameAction() {
                @Override public void update() {
                    if (!player.isDead) player.increaseMaxHp(33, true);
                    this.isDone = true;
                }
            });
            addToTop(new GainGoldWithAnimationAction(777));
            for (AbstractPower power : new ArrayList<>(player.powers)) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    addToTop(new RemoveSpecificPowerAction(player, player, power));
                }
            }
            return;
        }
        if (stage == 4) {
            addToTop(new PressEndTurnButtonAction());
            return;
        }
        if (stage == 3) player.loseGold(Math.min(20, Math.max(0, player.gold)));
        if (stage == 2) loseMaxHealth(player, 5);
        if (player.isDead) return;
        addToTop(new DrawCardAction(player, draw));
        switch (stage) {
            case 7:
                addToTop(new ApplyPowerAction(player, player, new FrailPower(player, 6, false), 6));
                break;
            case 6:
                addToTop(new ApplyPowerAction(player, player, new VulnerablePower(player, 6, false), 6));
                addToTop(new ApplyPowerAction(player, player, new WeakPower(player, 6, false), 6));
                break;
            case 5:
                addToTop(new ApplyPowerAction(player, player, new OriginalSinAttackOnlyPower(player), 1));
                break;
            case 1:
                addToTop(new ApplyPowerAction(player, player, new OriginalSinNoAttackPower(player), 1));
                break;
            default:
                break;
        }
    }

    public static void loseMaxHealth(AbstractPlayer player, int amount) {
        // Native decreaseMaxHealth clamps max HP to 1 and does not inflict HP loss.
        player.maxHealth = Math.max(0, player.maxHealth - amount);
        player.damage(new DamageInfo(null, amount, DamageInfo.DamageType.HP_LOSS));
        player.currentHealth = Math.min(player.currentHealth, player.maxHealth);
        player.healthBarUpdatedEvent();
        // Revival may return early from damage(), but cannot heal above a zero maximum.
        if (player.maxHealth == 0) {
            player.currentHealth = 0;
            if (!player.isDead) {
                player.isDead = true;
                player.loseBlock();
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            }
        }
    }
}
