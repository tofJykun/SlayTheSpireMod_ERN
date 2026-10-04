package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.PlatingPower;

public class PlatingPotion extends AbstractPotion {
    public static final String POTION_ID = "PlatingPotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public PlatingPotion() {
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
        PowerStrings power = CardCrawlGame.languagePack.getPowerStrings(PlatingPower.POWER_ID);
        tips.add(new PowerTip(power.NAME, power.DESCRIPTIONS[0] + potency + power.DESCRIPTIONS[1]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new PlatingPower(AbstractDungeon.player, potency), potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) { return 2; }

    @Override
    public AbstractPotion makeCopy() { return new PlatingPotion(); }
}
