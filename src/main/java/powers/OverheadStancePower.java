package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class OverheadStancePower extends AbstractPower {
    public static final String POWER_ID = "OverheadStancePower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private UseCardAction attackAction;
    private boolean triggered;
    private boolean finished;
    private boolean finishQueued;
    private int poiseBreak;

    public OverheadStancePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.fontScale = 8.0F;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.triggered || card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        this.triggered = true;
        this.attackAction = action;
        int cost = Math.max(0, card.costForTurn);
        if (card.cost == -1) {
            cost = Math.max(0, card.energyOnUse < 0 ? EnergyPanel.totalCount : card.energyOnUse);
            if (AbstractDungeon.player.hasRelic("Chemical X")) {
                cost += 2;
            }
        }
        this.poiseBreak = cost * this.amount;
        flash();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!this.triggered || this.finished || this.poiseBreak <= 0 || info.owner != this.owner
                || info.type != DamageInfo.DamageType.NORMAL || !(target instanceof AbstractMonster)
                || target.isDeadOrEscaped()) {
            return;
        }
        addToTop(new ApplyPowerAction(target, this.owner,
                new PoiseBreakPower(target, this.poiseBreak), this.poiseBreak));
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!this.triggered || action != this.attackAction || this.finishQueued) {
            return;
        }
        this.finishQueued = true;
        // X-cost actions can append their hits behind UseCardAction (e.g. Whirlwind).
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                finished = true;
                addToTop(new RemoveSpecificPowerAction(owner, owner, OverheadStancePower.this));
                this.isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = STRINGS.DESCRIPTIONS[0] + this.amount + STRINGS.DESCRIPTIONS[1];
    }
}
