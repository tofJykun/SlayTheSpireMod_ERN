package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.BloodburnPower;

public class BlackflamePotion extends AbstractPotion {
    public static final String POTION_ID = "BlackflamePotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BlackflamePotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.WHITE);
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BloodburnPower.NAME,
                BloodburnPower.DESCRIPTIONS[2] + potency + BloodburnPower.DESCRIPTIONS[1]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null && target != null) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player,
                    new BloodburnPower(target, AbstractDungeon.player, potency), potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 6;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BlackflamePotion();
    }
}
