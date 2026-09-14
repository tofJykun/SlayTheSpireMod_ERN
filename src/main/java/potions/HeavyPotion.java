package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.PoiseBreakPower;

public class HeavyPotion extends AbstractPotion {
    public static final String POTION_ID = "HeavyPotion";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_POISE_BREAK = 5;

    public HeavyPotion() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.H, PotionColor.WHITE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency + POTION_STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(PoiseBreakPower.NAME, PoiseBreakPower.DESCRIPTIONS[0]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null && target != null) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player,
                    new PoiseBreakPower(target, this.potency), this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_POISE_BREAK;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new HeavyPotion();
    }
}
