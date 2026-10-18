package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;

public class AnalyzingPower extends AbstractPower {
    public static final String POWER_ID = "AnalyzingPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int ENERGY = 1;
    private static final int SLEEP = 1;

    public AnalyzingPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return card == null || card.type != AbstractCard.CardType.ATTACK;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
    }

    @Override
    public void atStartOfTurn() {
        if (!CombatState.isInCombat() || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        flash();
        addToBot(new GainEnergyAction(ENERGY));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, owner, new SleepPower(monster, SLEEP), SLEEP));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(STRINGS.DESCRIPTIONS[0], ENERGY, SLEEP);
    }
}
