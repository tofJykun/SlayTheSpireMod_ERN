package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import general.CrudeDrug;
import general.SmithingBody;

public class DragonKingGreataxePower extends AbstractPower {
    public static final String POWER_ID = "DragonKingGreataxePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DragonKingGreataxePower(AbstractCreature owner, int turns) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = turns;
        type = PowerType.DEBUFF;
        isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static boolean blocksPotions() {
        if (CardCrawlGame.loadingSave || !CombatState.isInCombat()
                || CombatState.currentRoom().isBattleOver || AbstractDungeon.player == null) return false;
        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        return power != null && power.amount > 0;
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return amount <= 0 || card == null || !CrudeDrug.isCrudeDrugCard(SmithingBody.behavior(card));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, POWER_ID, 1));
    }

    @Override
    public void onVictory() {
        amount = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
