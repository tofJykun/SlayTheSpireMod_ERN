package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class CeruleanwhorlBubblePower extends AbstractPower {
    public static final String POWER_ID = "CeruleanwhorlBubblePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public CeruleanwhorlBubblePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount <= 0 || card == null) {
            return;
        }

        flash();
        int energyGain = getEnergyValue(card);
        card.freeToPlayOnce = true;
        if (energyGain > 0) {
            addToBot((AbstractGameAction)new GainEnergyAction(energyGain));
        }

        this.amount--;
        updateDescription();
        if (this.amount <= 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public static boolean canPlayThroughBubble(AbstractCard card) {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(POWER_ID)) {
            return false;
        }
        if (AbstractDungeon.actionManager.turnHasEnded) {
            return false;
        }
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (!power.canPlayCard(card)) {
                return false;
            }
        }
        if (AbstractDungeon.player.hasPower("Entangled") && card.type == AbstractCard.CardType.ATTACK) {
            return false;
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (!relic.canPlay(card)) {
                return false;
            }
        }
        for (AbstractBlight blight : AbstractDungeon.player.blights) {
            if (!blight.canPlay(card)) {
                return false;
            }
        }
        for (AbstractCard handCard : AbstractDungeon.player.hand.group) {
            if (!handCard.canPlay(card)) {
                return false;
            }
        }
        card.cantUseMessage = null;
        return true;
    }

    private static int getEnergyValue(AbstractCard card) {
        if (card.costForTurn == -1 || card.cost == -1) {
            return EnergyPanel.getCurrentEnergy();
        }
        return Math.max(card.costForTurn, 0);
    }
}
