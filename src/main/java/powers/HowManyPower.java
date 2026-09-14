package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HowManyPower extends AbstractPower {
    public static final String POWER_ID = "HowManyPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int ENERGY_THRESHOLD = 4;
    private static int powerIdOffset;

    public HowManyPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID + powerIdOffset;
        powerIdOffset++;
        this.owner = owner;
        this.amount = 0;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int spentEnergy = getSpentEnergy(card);
        if (spentEnergy <= 0) {
            return;
        }

        int total = this.amount + spentEnergy;
        int energyGain = total / ENERGY_THRESHOLD;
        this.amount = total % ENERGY_THRESHOLD;
        if (energyGain > 0) {
            flash();
            addToBot((AbstractGameAction)new GainEnergyAction(energyGain));
        }
        updateDescription();
    }

    private int getSpentEnergy(AbstractCard card) {
        if (card == null || card.freeToPlay() || card.isInAutoplay) {
            return 0;
        }
        if (card.type == AbstractCard.CardType.SKILL
                && AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower("Corruption")) {
            return 0;
        }
        if (card.cost == -1) {
            return Math.max(0, card.energyOnUse);
        }
        return Math.max(0, card.costForTurn);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + ENERGY_THRESHOLD + DESCRIPTIONS[1]
                + this.amount + DESCRIPTIONS[2];
    }
}
