package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.actions.watcher.JudgementAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import general.BulkPotionQueue;
import powers.BloodburnPower;
import powers.HoverPower;
import powers.MajorArcanaSunPower;
import powers.MajorArcanaTemperancePower;

import java.util.ArrayList;

public class MajorArcanaAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster monster;
    private final int stage;
    private final int block;

    public MajorArcanaAction(AbstractPlayer player, AbstractMonster monster, int stage, int block, int value) {
        this.player = player;
        this.monster = monster;
        this.stage = stage;
        this.block = block;
        this.amount = value;
    }

    private void self(AbstractPower power) { addToTop(new ApplyPowerAction(player, player, power, power.amount)); }
    private static boolean alive(AbstractMonster m) { return m != null && !m.isDeadOrEscaped() && !m.halfDead; }

    @Override public void update() {
        if (isDone) return;
        isDone = true;
        if (player == null || player.isDead) return;
        // Children go before UseCardAction and any later queued card effects.
        switch (stage) {
            case 1: addToTop(new GainBlockAction(player, player, block)); break;
            case 2: addToTop(new DrawCardAction(player, amount)); break;
            case 3:
                if (alive(monster)) {
                    addToTop(new ApplyPowerAction(monster, player, new VulnerablePower(monster, amount, false), amount));
                    addToTop(new ApplyPowerAction(monster, player, new WeakPower(monster, amount, false), amount));
                }
                break;
            case 4: self(new EquilibriumPower(player, 1)); break;
            case 5:
                self(new EnergizedPower(player, amount));
                self(new DrawCardNextTurnPower(player, amount));
                break;
            case 6: addToTop(new DiscoveryAction()); break;
            case 7: addToTop(new GainEnergyAction(amount)); break;
            case 8: self(new BlurPower(player, amount)); break;
            case 9: self(new StrengthPower(player, amount)); break;
            case 10: self(new DexterityPower(player, amount)); break;
            case 11: addToTop(new TransformAllHandAction()); break;
            case 12:
                for (AbstractPower power : new ArrayList<>(player.powers)) {
                    if (power.type == AbstractPower.PowerType.DEBUFF)
                        addToTop(new RemoveSpecificPowerAction(player, player, power));
                }
                break;
            case 13: self(new HoverPower(player, amount)); break;
            case 14:
                if (alive(monster)) addToTop(new ApplyPowerAction(monster, player, new BloodburnPower(monster, player, amount), amount));
                break;
            case 15:
                addToTop(new PressEndTurnButtonAction());
                self(new MajorArcanaTemperancePower(player));
                break;
            case 16:
                for (AbstractCard card : new ArrayList<>(player.hand.group))
                    addToTop(new ExhaustSpecificCardAction(card, player.hand));
                break;
            case 17: self(new TheBombPower(player, 3, amount)); break;
            case 18:
                for (int i = 0; i < amount; i++) addToTop(new ObtainPotionAction(BulkPotionQueue.getRandomBulkPotion()));
                break;
            case 19:
                for (AbstractMonster enemy : AbstractDungeon.getMonsters().monsters) {
                    if (alive(enemy)) addToTop(new ApplyPowerAction(enemy, player, new StrengthPower(enemy, -amount), -amount));
                }
                self(new StrengthPower(player, -2));
                break;
            case 20: self(new MajorArcanaSunPower(player, amount)); break;
            case 21:
                for (AbstractMonster enemy : AbstractDungeon.getMonsters().monsters) {
                    if (alive(enemy) && enemy.hasPower(MinionPower.POWER_ID))
                        addToTop(new JudgementAction(enemy, Integer.MAX_VALUE));
                }
                break;
            case 22:
                addToTop(new ChannelAction(new Plasma()));
                addToTop(new ChannelAction(new Dark()));
                addToTop(new ChannelAction(new Frost()));
                addToTop(new ChannelAction(new Lightning()));
                break;
            default: break;
        }
    }
}
