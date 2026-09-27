package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.BlockedAttackDamagePatch;

public class CrystalRingShieldPower extends AbstractPower {
    public static final String POWER_ID = "CrystalRingShieldPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public CrystalRingShieldPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info != null && info.owner instanceof AbstractMonster
                && info.type == DamageInfo.DamageType.NORMAL) {
            int blockedDamage = BlockedAttackDamagePatch.getBlockedDamage(info);
            if (blockedDamage > 0) {
                flash();
                addToTop((AbstractGameAction)new LoseHPAction(info.owner, this.owner, blockedDamage,
                        AbstractGameAction.AttackEffect.FIRE));
            }
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
