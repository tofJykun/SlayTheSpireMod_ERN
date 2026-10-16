package powers;

import actions.TransformAllHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class PowerOfLiveInDeathPower extends AbstractPower {
    public static final String POWER_ID = "PowerOfLiveInDeathPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public PowerOfLiveInDeathPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (!CombatState.isInCombat()
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        flash();
        addToBot(new TransformAllHandAction());
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        amount = -1;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
