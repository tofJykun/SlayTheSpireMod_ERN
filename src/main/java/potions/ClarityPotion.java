package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.ClarityPotionPower;

public class ClarityPotion extends AbstractPotion {
    public static final String POTION_ID = "ClarityPotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public ClarityPotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.S, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency, potency, potency, potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new DrawCardAction(AbstractDungeon.player, potency));
            addToBot(new GainEnergyAction(potency));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new ClarityPotionPower(AbstractDungeon.player, potency), potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) { return 1; }

    @Override
    public AbstractPotion makeCopy() { return new ClarityPotion(); }
}
