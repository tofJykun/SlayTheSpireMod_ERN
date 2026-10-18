package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CrystalDartCombat;

public class CrystalDartPower extends AbstractPower {
    public static final String POWER_ID = "CrystalDartPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private AbstractMonster preferredTarget;
    private boolean expired;

    public CrystalDartPower(AbstractMonster owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() { preferredTarget = CrystalDartCombat.randomOther((AbstractMonster)owner); }

    @Override
    public void stackPower(int amount) { }

    public AbstractMonster target() {
        if (expired) return null;
        if (!CrystalDartCombat.living(preferredTarget)) {
            preferredTarget = CrystalDartCombat.randomOther((AbstractMonster)owner);
        }
        return preferredTarget;
    }

    @Override
    public void atEndOfRound() {
        expired = true;
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() { description = STRINGS.DESCRIPTIONS[0]; }
}
