package powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DragonHeadStonePower extends AbstractPower {
    public static final String POWER_ID = "DragonHeadStonePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class SkillCostFields {
        public static final SpireField<Boolean> taxed = new SpireField<Boolean>(() -> false);
        public static final SpireField<Integer> originalCostForTurn = new SpireField<Integer>(() -> 0);
        public static final SpireField<Boolean> originalCostModifiedForTurn = new SpireField<Boolean>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class ResetAttributesPatch {
        @SpirePostfixPatch
        public static void postfix(AbstractCard __instance) {
            clearSkillTax(__instance);
        }
    }

    public DragonHeadStonePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        updateCardsInHand();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        updateCardsInHand();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        updateCard(card);
    }

    @Override
    public void onDrawOrDiscard() {
        updateCardsInHand();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        updateCardsInHand();
        updateDescription();
    }

    @Override
    public void onRemove() {
        restoreSkillTaxes();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    private void updateCardsInHand() {
        if (AbstractDungeon.player == null) {
            return;
        }
        updateGroup(AbstractDungeon.player.hand);
    }

    private void updateGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            updateCard(card);
        }
    }

    private void updateCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        if (card.type == AbstractCard.CardType.ATTACK) {
            makeAttackFree(card);
        } else if (card.type == AbstractCard.CardType.SKILL) {
            increaseSkillCost(card);
        }
    }

    private static void makeAttackFree(AbstractCard card) {
        if (card.cost == -2 || card.costForTurn == 0) {
            return;
        }
        card.setCostForTurn(0);
    }

    private static void increaseSkillCost(AbstractCard card) {
        if (card.cost < 0 || card.costForTurn < 0 || SkillCostFields.taxed.get(card)) {
            return;
        }
        SkillCostFields.taxed.set(card, true);
        SkillCostFields.originalCostForTurn.set(card, card.costForTurn);
        SkillCostFields.originalCostModifiedForTurn.set(card, card.isCostModifiedForTurn);
        card.setCostForTurn(card.costForTurn + 1);
    }

    private static void clearSkillTax(AbstractCard card) {
        SkillCostFields.taxed.set(card, false);
        SkillCostFields.originalCostForTurn.set(card, 0);
        SkillCostFields.originalCostModifiedForTurn.set(card, false);
    }

    private void restoreSkillTaxes() {
        if (AbstractDungeon.player == null) {
            return;
        }
        restoreSkillTaxes(AbstractDungeon.player.hand);
    }

    private static void restoreSkillTaxes(CardGroup group) {
        for (AbstractCard card : group.group) {
            if (SkillCostFields.taxed.get(card)) {
                card.costForTurn = SkillCostFields.originalCostForTurn.get(card);
                card.isCostModifiedForTurn = SkillCostFields.originalCostModifiedForTurn.get(card);
                clearSkillTax(card);
            }
        }
    }
}
