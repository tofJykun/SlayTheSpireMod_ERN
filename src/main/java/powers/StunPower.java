package powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StunPower extends AbstractPower {
    public static final String POWER_ID = "StunPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private EnemyMoveInfo savedMove;
    private String savedMoveName;
    private boolean saved;

    public StunPower(AbstractMonster owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        loadRegion("confusion");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        saveAndReplaceIntent();
    }

    @Override
    public void atEndOfRound() {
        if (this.amount <= 1) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void onRemove() {
        restoreIntent();
    }

    private void saveAndReplaceIntent() {
        if (this.saved || !(this.owner instanceof AbstractMonster)) {
            return;
        }

        AbstractMonster monster = (AbstractMonster)this.owner;
        this.savedMove = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
        this.savedMoveName = monster.moveName;
        this.saved = true;

        if (this.savedMove == null) {
            return;
        }

        EnemyMoveInfo stunMove = new EnemyMoveInfo(this.savedMove.nextMove, AbstractMonster.Intent.STUN,
                -1, 0, false);
        ReflectionHacks.setPrivate(monster, AbstractMonster.class, "move", stunMove);
        monster.moveName = null;
        monster.createIntent();
        monster.applyPowers();
    }

    private void restoreIntent() {
        if (!this.saved || !(this.owner instanceof AbstractMonster) || this.savedMove == null) {
            return;
        }

        AbstractMonster monster = (AbstractMonster)this.owner;
        if (monster.isDeadOrEscaped()) {
            return;
        }

        ReflectionHacks.setPrivate(monster, AbstractMonster.class, "move", this.savedMove);
        monster.moveName = this.savedMoveName;
        monster.createIntent();
        monster.applyPowers();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
