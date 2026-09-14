package powers;

import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MoonOfNokstellaPower extends AbstractPower {
    public static final String POWER_ID = "MoonOfNokstellaPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public MoonOfNokstellaPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static int currentReduction() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(POWER_ID)) {
            return 0;
        }
        return Math.max(0, AbstractDungeon.player.getPower(POWER_ID).amount);
    }

    @Override
    public void onInitialApplication() {
        refreshCraftmanCreations();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
        refreshCraftmanCreations();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        refreshCard(card);
    }

    @Override
    public void onDrawOrDiscard() {
        refreshCraftmanCreations();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    private static void refreshCraftmanCreations() {
        if (AbstractDungeon.player == null) {
            return;
        }
        refreshGroup(AbstractDungeon.player.hand);
        refreshGroup(AbstractDungeon.player.drawPile);
        refreshGroup(AbstractDungeon.player.discardPile);
        refreshGroup(AbstractDungeon.player.exhaustPile);
    }

    private static void refreshGroup(CardGroup group) {
        if (group == null) {
            return;
        }
        for (AbstractCard card : group.group) {
            refreshCard(card);
        }
    }

    private static void refreshCard(AbstractCard card) {
        if (card instanceof CraftmanCreation) {
            ((CraftmanCreation)card).refreshFromParts();
        }
    }
}
