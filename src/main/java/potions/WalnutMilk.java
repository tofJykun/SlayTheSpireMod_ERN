package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.CombatState;
import powers.FaithPower;
import powers.SpiritPower;

public class WalnutMilk extends AbstractPotion {
    public static final String POTION_ID = "WalnutMilk";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public WalnutMilk() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.JAR, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(SpiritPower.NAME, SpiritPower.DESCRIPTIONS[0] + potency + SpiritPower.DESCRIPTIONS[1]));
        tips.add(new PowerTip(FaithPower.NAME, FaithPower.DESCRIPTIONS[0] + potency + FaithPower.DESCRIPTIONS[2]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) return;
        final int perEnergy = potency;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (!CombatState.isInCombat() || AbstractDungeon.player == null) return;
                int energy = Math.max(0, EnergyPanel.totalCount);
                if (energy == 0) return;
                // Read and spend together, so repeated potion uses cannot convert the same energy twice.
                AbstractDungeon.player.energy.use(energy);
                int amount = energy * perEnergy;
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new FaithPower(AbstractDungeon.player, amount), amount));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new SpiritPower(AbstractDungeon.player, amount), amount));
            }
        });
    }

    @Override
    public int getPotency(int ascensionLevel) { return 2; }

    @Override
    public AbstractPotion makeCopy() { return new WalnutMilk(); }
}
