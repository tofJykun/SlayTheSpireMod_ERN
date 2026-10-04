package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.DarkswordDescriptionPatch;

public class DarkswordPower extends AbstractPower {
    public static final String POWER_ID = "DarkswordPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DarkswordPower(AbstractCreature owner, int amount) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        refreshCombatCardDescriptions();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
        refreshCombatCardDescriptions();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (float)Math.pow(2.0D, this.amount);
        }
        return damage;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void onRemove() {
        DarkswordDescriptionPatch.restoreDescriptions();
    }

    @Override
    public void onVictory() {
        // Victory can occur before the end-of-turn removal action runs.
        DarkswordDescriptionPatch.restoreDescriptions();
    }

    @Override
    public void updateDescription() {
        int multiplier = (int)Math.pow(2.0D, this.amount);
        this.description = POWER_STRINGS.DESCRIPTIONS[0] + multiplier + POWER_STRINGS.DESCRIPTIONS[1];
    }

    public static void refreshCombatCardDescriptions() {
        if (AbstractDungeon.player == null) {
            return;
        }
        refreshGroup(AbstractDungeon.player.hand);
        refreshGroup(AbstractDungeon.player.drawPile);
        refreshGroup(AbstractDungeon.player.discardPile);
        refreshGroup(AbstractDungeon.player.exhaustPile);
        refreshGroup(AbstractDungeon.player.limbo);
    }

    private static void refreshGroup(CardGroup group) {
        if (group == null) {
            return;
        }
        for (AbstractCard card : group.group) {
            if (card.rawDescription != null) {
                card.initializeDescription();
            }
        }
    }
}
