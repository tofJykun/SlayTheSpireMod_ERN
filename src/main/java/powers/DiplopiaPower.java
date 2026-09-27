package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

public class DiplopiaPower extends AbstractPower {
    public static final String POWER_ID = "DiplopiaPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean attackCardActive;

    public DiplopiaPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.attackCardActive = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (this.attackCardActive && type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F;
        }
        return damage;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (card != null && card.type == AbstractCard.CardType.ATTACK
                && type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F;
        }
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.attackCardActive = card != null && card.type == AbstractCard.CardType.ATTACK;
        if (this.attackCardActive) {
            flash();
            // Match Pen Nib: keep the bonus until this attack's queued effects finish.
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
