package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import general.CombatState;

public class EndurancePotion extends AbstractPotion {
    public static final String POTION_ID = "EndurancePotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public EndurancePotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.S, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(GameDictionary.RETAIN.NAMES[0], GameDictionary.RETAIN.DESCRIPTION));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new EquilibriumPower(AbstractDungeon.player, potency), potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) { return 3; }

    @Override
    public AbstractPotion makeCopy() { return new EndurancePotion(); }
}
