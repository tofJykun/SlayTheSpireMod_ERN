package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.BloodlossPower;
import powers.MadnessPower;
import powers.FrostbitePower;
import powers.SleepPower;

public class CalamityPotion extends AbstractPotion {
    public static final String POTION_ID = "CalamityPotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public CalamityPotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.MOON, PotionColor.WHITE);
        isThrown = true;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BloodlossPower.NAME, BloodlossPower.DESCRIPTIONS[0]));
        tips.add(new PowerTip(MadnessPower.NAME, MadnessPower.DESCRIPTIONS[0]));
        tips.add(new PowerTip(FrostbitePower.NAME, FrostbitePower.DESCRIPTIONS[0]));
        tips.add(new PowerTip(SleepPower.NAME, SleepPower.DESCRIPTIONS[0]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new ApplyPowerAction(monster, AbstractDungeon.player,
                        new BloodlossPower(monster, potency), potency));
                addToBot(new ApplyPowerAction(monster, AbstractDungeon.player,
                        new MadnessPower(monster, potency), potency));
                addToBot(new ApplyPowerAction(monster, AbstractDungeon.player,
                        new FrostbitePower(monster, potency), potency));
                addToBot(new ApplyPowerAction(monster, AbstractDungeon.player,
                        new SleepPower(monster, potency), potency));
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new CalamityPotion();
    }
}
