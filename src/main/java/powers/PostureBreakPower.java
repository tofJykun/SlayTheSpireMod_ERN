package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PostureBreakPower extends AbstractPower {
    public static final String POWER_ID = "PostureBreakPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    private boolean attackTurn;
    private boolean causedHealthLoss;

    public PostureBreakPower(AbstractMonster owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.DEBUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = 1;
        updateDescription();
    }

    public static void beginTurn(AbstractMonster monster) {
        AbstractPower power = monster.getPower(POWER_ID);
        if (!(power instanceof PostureBreakPower)) {
            return;
        }
        PostureBreakPower posture = (PostureBreakPower)power;
        AbstractMonster.Intent intent = monster.intent;
        posture.attackTurn = intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND;
        posture.causedHealthLoss = false;
    }

    @Override
    public void atEndOfRound() {
        // All monsters' queued hits and next-intent rolls have finished here.
        // Applying Stun now skips the NEXT enemy turn, not the completed one.
        if (this.attackTurn && !this.causedHealthLoss && this.owner instanceof AbstractMonster
                && !this.owner.isDeadOrEscaped()
                && !this.owner.hasPower(DelayedStunPower.POWER_ID)
                && !this.owner.hasPower(StunPower.POWER_ID)) {
            flash();
            addToBot(new ApplyPowerAction(this.owner, AbstractDungeon.player,
                    new StunPower((AbstractMonster)this.owner, 1), 1));
        }
        this.attackTurn = false;
        this.causedHealthLoss = false;
    }

    public static void recordHealthWrite(AbstractCreature target, DamageInfo info, int newHealth) {
        if (!(target instanceof AbstractPlayer) || info == null || !(info.owner instanceof AbstractMonster)
                || info.type != DamageInfo.DamageType.NORMAL
                || Math.max(0, newHealth) >= Math.max(0, target.currentHealth)) {
            return;
        }

        // Posture Break belongs to the attacker, not the player receiving damage.
        AbstractPower power = info.owner.getPower(POWER_ID);
        if (power instanceof PostureBreakPower) {
            PostureBreakPower posture = (PostureBreakPower)power;
            if (posture.attackTurn) {
                posture.causedHealthLoss = true;
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
