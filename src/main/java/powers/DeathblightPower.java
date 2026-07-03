package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DeathblightPower extends AbstractPower {
    public static final String POWER_ID = "DeathblightPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int DEATHBLIGHT_THRESHOLD = 10;
    private static final int PLAYER_DAMAGE = 99999;

    public DeathblightPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        loadRegion("poison");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount < DEATHBLIGHT_THRESHOLD || this.owner.isDeadOrEscaped()) {
            return;
        }

        flash();
        addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        if (this.owner instanceof AbstractMonster) {
            addToBot((AbstractGameAction)new InstantKillAction(this.owner));
        } else {
            addToBot((AbstractGameAction)new LoseHPAction(this.owner, this.owner, PLAYER_DAMAGE));
        }
    }

    @Override
    public void updateDescription() {
        if (this.owner instanceof AbstractMonster) {
            this.description = DESCRIPTIONS[0] + DEATHBLIGHT_THRESHOLD + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + DEATHBLIGHT_THRESHOLD + DESCRIPTIONS[2] + PLAYER_DAMAGE
                    + DESCRIPTIONS[3];
        }
    }
}
