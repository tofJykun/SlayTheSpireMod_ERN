package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.VirtualCurrencyPower;

public class PlacerGoldPotion extends AbstractPotion {
    public static final String POTION_ID = "PlacerGoldPotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public PlacerGoldPotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(VirtualCurrencyPower.NAME, VirtualCurrencyPower.DESCRIPTIONS[0]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new VirtualCurrencyPower(AbstractDungeon.player, potency), potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 30;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new PlacerGoldPotion();
    }
}
