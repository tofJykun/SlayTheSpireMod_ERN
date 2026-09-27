package potions;

import actions.OrderPotionAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;

public class OrderPotion extends AbstractPotion {
    public static final String POTION_ID = "OrderPotion";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_DRAW = 2;
    private static final int BASE_DISCARD = 1;

    public OrderPotion() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.S, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        int discardAmount = this.potency / BASE_DRAW * BASE_DISCARD;
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency
                + POTION_STRINGS.DESCRIPTIONS[1] + discardAmount + POTION_STRINGS.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new DrawCardAction(AbstractDungeon.player, this.potency));
            addToBot(new OrderPotionAction(this.potency / BASE_DRAW * BASE_DISCARD));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_DRAW;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new OrderPotion();
    }
}
