package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.CompanionJarPower;

public class ExplosivePotion extends AbstractPotion {
    public static final String POTION_ID = "ExplosivePotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public ExplosivePotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.H, PotionColor.FIRE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = STRINGS.DESCRIPTIONS[0] + this.potency + STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            // PotionHistory notifies existing powers before this queued application resolves.
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new CompanionJarPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 13;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ExplosivePotion();
    }
}
