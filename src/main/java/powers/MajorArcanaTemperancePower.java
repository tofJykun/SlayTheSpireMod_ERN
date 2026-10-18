package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MajorArcanaTemperancePower extends AbstractPower {
    public static final String POWER_ID = "MajorArcanaTemperancePower";
    private boolean expired;

    public MajorArcanaTemperancePower(AbstractCreature owner) {
        ID = POWER_ID;
        name = CardCrawlGame.languagePack.getPowerStrings(ID).NAME;
        this.owner = owner;
        amount = 50;
        type = PowerType.BUFF;
        isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public int reduce(int damage, DamageInfo.DamageType type) {
        return expired || type == DamageInfo.DamageType.HP_LOSS ? damage : Math.max(0, damage / 2);
    }

    @Override public void stackPower(int value) { fontScale = 8.0F; }
    @Override public void atStartOfTurn() {
        expired = true;
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }
    @Override public void updateDescription() { description = CardCrawlGame.languagePack.getPowerStrings(ID).DESCRIPTIONS[0]; }
}
