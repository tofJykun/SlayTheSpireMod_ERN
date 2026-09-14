package potions;

import actions.SmithingMaterialDiscoveryAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;

public class PearlPotion extends AbstractPotion {
    public static final String POTION_ID = "PearlPotion";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public PearlPotion() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.CARD, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[this.potency > 1 ? 1 : 0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new SmithingMaterialDiscoveryAction(this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new PearlPotion();
    }
}
