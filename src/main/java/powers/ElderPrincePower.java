package powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ElderPrincePower extends AbstractPower {
    public static final String POWER_ID = "ElderPrincePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered;

    public ElderPrincePower(AbstractCreature owner) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = -1;
        // Like Blasphemy, the lethal drawback is not blocked by Artifact.
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        // This hook is after Block, Buffer, Tungsten Rod and onLoseHp reductions.
        if (!triggered && damageAmount > 0 && info != null && info.type == DamageInfo.DamageType.NORMAL
                && info.owner != null && info.owner != owner) {
            triggered = true;
            flash();
            addToTop(new LoseHPAction(owner, owner, 99999));
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        amount = -1;
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
