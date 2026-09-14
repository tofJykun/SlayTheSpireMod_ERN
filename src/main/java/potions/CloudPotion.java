package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.HoverPower;

public class CloudPotion extends AbstractPotion {
    public static final String POTION_ID = "CloudPotion";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_HOVER = 2;

    public CloudPotion() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency + POTION_STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(HoverPower.NAME,
                HoverPower.DESCRIPTIONS[0] + this.potency + HoverPower.DESCRIPTIONS[1] + HoverPower.DESCRIPTIONS[2]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new HoverPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_HOVER;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new CloudPotion();
    }
}
