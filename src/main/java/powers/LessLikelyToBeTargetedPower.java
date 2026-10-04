package powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LessLikelyToBeTargetedPower extends AbstractPower {
    public static final String POWER_ID = "LessLikelyToBeTargetedPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private EnemyMoveInfo savedMove;
    private EnemyMoveInfo replacement;
    private String savedMoveName;
    private boolean acted;

    public LessLikelyToBeTargetedPower(AbstractMonster owner, int block) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = block;
        this.type = PowerType.DEBUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    @Override
    public void onInitialApplication() {
        AbstractMonster monster = (AbstractMonster)owner;
        savedMove = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
        if (savedMove == null || !isAttackIntent(monster.intent)) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            return;
        }
        savedMoveName = monster.moveName;
        replacement = new EnemyMoveInfo(savedMove.nextMove, AbstractMonster.Intent.DEFEND, -1, 0, false);
        ReflectionHacks.setPrivate(monster, AbstractMonster.class, "move", replacement);
        monster.moveName = name;
        monster.createIntent();
        monster.applyPowers();
    }

    @Override
    public void stackPower(int stackAmount) {
        // This replaces one action, rather than adding block or delaying it again.
    }

    public static boolean isReplacingIntent(AbstractMonster monster) {
        AbstractPower power = monster.getPower(POWER_ID);
        if (!(power instanceof LessLikelyToBeTargetedPower)) {
            return false;
        }
        LessLikelyToBeTargetedPower replacementPower = (LessLikelyToBeTargetedPower)power;
        return replacementPower.replacement != null && replacementPower.replacement
                == ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
    }

    public static boolean takeTurn(AbstractMonster monster) {
        if (!isReplacingIntent(monster)) {
            return false;
        }
        LessLikelyToBeTargetedPower power = (LessLikelyToBeTargetedPower)monster.getPower(POWER_ID);
        if (!power.acted) {
            power.acted = true;
            power.flash();
            power.addToBot(new GainBlockAction(monster, monster, power.amount));
        }
        return true;
    }

    @Override
    public void atEndOfRound() {
        // Stun may postpone this replacement too. Do not consume it before it acts.
        if (acted || (!owner.hasPower(StunPower.POWER_ID) && !isReplacingIntent((AbstractMonster)owner))) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onRemove() {
        AbstractMonster monster = (AbstractMonster)owner;
        if (savedMove != null && !monster.isDeadOrEscaped() && replacement != null
                && replacement == ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move")) {
            // A death/phase transition that installed another move must not be overwritten.
            ReflectionHacks.setPrivate(monster, AbstractMonster.class, "move", savedMove);
            monster.moveName = savedMoveName;
            monster.createIntent();
            monster.applyPowers();
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }
}
