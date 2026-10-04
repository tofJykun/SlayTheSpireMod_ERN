package powers;

import actions.NineStagesOfDecayAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.CombatState;

public class NineStagesOfDecayPower extends AbstractPower {
    public static final String POWER_ID = "NineStagesOfDecayPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractRoom room;

    public NineStagesOfDecayPower(AbstractCreature owner, AbstractRoom room) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.room = room;
        this.amount = 9;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // A later countdown cannot finish sooner than the one already running.
        flash();
    }

    @Override
    public void atStartOfTurn() {
        if (amount <= 0 || CombatState.currentRoom() != room || !NineStagesOfDecayAction.isEligible(room)) {
            return;
        }
        amount--;
        updateDescription();
        if (amount == 0) {
            flash();
            addToBot(new NineStagesOfDecayAction(room));
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
