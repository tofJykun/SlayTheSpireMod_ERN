package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.StunPower;

public class YoungWhiteBranchPower extends AbstractPower {
    public static final String POWER_ID = "YoungWhiteBranchPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final boolean affectsAll;

    public YoungWhiteBranchPower(AbstractCreature owner, boolean affectsAll) {
        this.name = STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.affectsAll = affectsAll;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        if (affectsAll) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(monster, owner,
                            new StunPower(monster, 1), 1));
                }
            }
        } else {
            AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
                    AbstractDungeon.cardRandomRng);
            if (monster != null) {
                addToBot(new ApplyPowerAction(monster, owner,
                        new StunPower(monster, 1), 1));
            }
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount = -1;
    }

    @Override
    public void updateDescription() {
        this.description = affectsAll ? STRINGS.DESCRIPTIONS[1] : STRINGS.DESCRIPTIONS[0];
    }
}
