package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.GetLostPatch;

public class GetLostPower extends LessLikelyToBeTargetedPower {
    public static final String POWER_ID = "GetLostPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int lastHealth;
    private boolean expiring;

    public GetLostPower(AbstractMonster owner) {
        super(owner, 30);
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.lastHealth = owner.currentHealth;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        this.lastHealth = owner.currentHealth;
        updateDescription();
    }

    public void onHealthChanged() {
        if (owner.currentHealth < lastHealth && !expiring) {
            expiring = true;
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        lastHealth = owner.currentHealth;
    }

    public void onPlayerTurnBoundary(boolean endOfTurn) {
        if (endOfTurn != GetLostPatch.triggersAtEnd()) return;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                onHealthChanged();
                AbstractMonster monster = (AbstractMonster)owner;
                if (expiring || monster.isDeadOrEscaped() || monster.halfDead
                        || owner.getPower(POWER_ID) != GetLostPower.this
                        || monster.hasPower(StunPower.POWER_ID)
                        || isReplacingIntent(monster)) return;
                flash();
                replaceIntent(false);
            }
        });
    }

    @Override
    public void atEndOfRound() {
        if (acted) restoreIntent();
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[GetLostPatch.triggersAtEnd() ? 1 : 0]
                + this.amount + STRINGS.DESCRIPTIONS[2];
    }
}
