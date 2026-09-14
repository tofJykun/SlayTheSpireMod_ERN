package powers;

import actions.HandAxeReturnAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.UUID;

public class HandAxeReturnPower extends AbstractPower {
    public static final String POWER_ID = "HandAxeReturnPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final ArrayList<UUID> cardUuids = new ArrayList<>();

    public HandAxeReturnPower(AbstractCreature owner, UUID cardUuid) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        addCard(cardUuid);
    }

    public void addCard(UUID cardUuid) {
        if (cardUuid != null) {
            this.cardUuids.add(cardUuid);
            this.amount = this.cardUuids.size();
            this.fontScale = 8.0F;
            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn() {
        if (this.cardUuids.isEmpty()) {
            return;
        }
        flash();
        addToBot((AbstractGameAction)new HandAxeReturnAction(this.cardUuids));
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
