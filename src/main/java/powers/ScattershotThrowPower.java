package powers;

import actions.ScattershotThrowAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import potions.ElixirOfLife;

public class ScattershotThrowPower extends AbstractPower {
    public static final String POWER_ID = "ScattershotThrowPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ScattershotThrowPower(AbstractCreature owner, int extraUses) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = extraUses;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        amount += stackAmount;
        fontScale = 8.0F;
        updateDescription();
    }

    public static void onPotionUsed(AbstractPotion potion, AbstractMonster target) {
        if (potion == null || AbstractDungeon.player == null || !CombatState.isInCombat()) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        if (!(power instanceof ScattershotThrowPower) || power.amount <= 0) {
            return;
        }
        int extraUses = power.amount;
        // Reserve the next use immediately, before any queued potion effects can trigger another use.
        power.amount = 0;
        power.updateDescription();
        power.flash();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(power.owner, power.owner, power));
        if (!"FairyPotion".equals(potion.ID) && !ElixirOfLife.POTION_ID.equals(potion.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ScattershotThrowAction(potion.ID, target, extraUses));
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + (amount + 1) + STRINGS.DESCRIPTIONS[1];
    }
}
