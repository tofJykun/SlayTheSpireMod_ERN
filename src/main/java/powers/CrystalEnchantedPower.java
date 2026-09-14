package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CrystalEnchantedPower extends AbstractPower {
    public static final String POWER_ID = "CrystalEnchantedPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int powerIdOffset;
    private final int cardThreshold;
    private int strengthToReturn;
    private int dexterityToReturn;

    public CrystalEnchantedPower(AbstractCreature owner, int cardThreshold) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID + powerIdOffset++;
        this.owner = owner;
        this.cardThreshold = cardThreshold;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount++;
        if (this.amount >= this.cardThreshold) {
            this.amount = 0;
            flash();
            loseStatTemporarily(true);
            loseStatTemporarily(false);
        }
        updateDescription();
    }

    private void loseStatTemporarily(final boolean strength) {
        final String statId = strength ? StrengthPower.POWER_ID : DexterityPower.POWER_ID;
        AbstractPower loss = strength ? new StrengthPower(this.owner, -1) : new DexterityPower(this.owner, -1);
        addToBot(new ApplyPowerAction(this.owner, this.owner, loss, -1) {
            private boolean recorded;

            @Override
            public void update() {
                if (this.recorded) {
                    super.update();
                    return;
                }
                int before = getStatAmount(statId);
                super.update();
                // Artifact may prevent the loss; refund only what was actually removed.
                int lost = Math.min(1, Math.max(0, before - getStatAmount(statId)));
                if (strength) {
                    strengthToReturn += lost;
                } else {
                    dexterityToReturn += lost;
                }
                this.recorded = true;
                updateDescription();
            }
        });
    }

    private int getStatAmount(String powerId) {
        AbstractPower power = this.owner.getPower(powerId);
        return power == null ? 0 : power.amount;
    }

    @Override
    public void atStartOfTurn() {
        this.amount = 0;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        if (this.strengthToReturn > 0) {
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new StrengthPower(this.owner, this.strengthToReturn), this.strengthToReturn));
        }
        if (this.dexterityToReturn > 0) {
            addToBot(new ApplyPowerAction(this.owner, this.owner,
                    new DexterityPower(this.owner, this.dexterityToReturn), this.dexterityToReturn));
        }
        this.strengthToReturn = 0;
        this.dexterityToReturn = 0;
        this.amount = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        String[] descriptions = POWER_STRINGS.DESCRIPTIONS;
        this.description = descriptions[0] + this.cardThreshold + descriptions[1]
                + this.amount + descriptions[2]
                + this.strengthToReturn + descriptions[3]
                + this.dexterityToReturn + descriptions[4];
    }
}
