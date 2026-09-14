package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.BlockedAttackDamagePatch;

public class ImmolationPower extends AbstractPower {
    public static final String POWER_ID = "ImmolationPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ImmolationPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        flash();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        boolean hadSummon = AbstractSummonPower.hasSummonPower(this.owner);
        if (hadSummon && info != null && info.owner instanceof AbstractMonster
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
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
