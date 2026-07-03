package potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class ElixirOfLife extends AbstractPotion {
    public static final String POTION_ID = "ElixirOfLife";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int GOLD_LOSS = 10;

    public ElixirOfLife() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.RARE, PotionSize.FAIRY, PotionColor.FAIRY);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        int healAmt = AbstractDungeon.player.maxHealth;
        if (healAmt < 1) {
            healAmt = 1;
        }
        AbstractDungeon.player.heal(healAmt, true);
        AbstractDungeon.topPanel.destroyPotion(this.slot);
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 100;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ElixirOfLife();
    }

    public static boolean triggerIfPresent() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.hasRelic("Mark of the Bloom")) {
            return false;
        }
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion != null && POTION_ID.equals(potion.ID)) {
                potion.flash();
                AbstractDungeon.player.currentHealth = 0;
                potion.use(AbstractDungeon.player);
                return true;
            }
        }
        return false;
    }

    public static void triggerEndOfPlayerTurnGoldLoss() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (int i = 0; i < AbstractDungeon.player.potions.size(); i++) {
            AbstractPotion potion = AbstractDungeon.player.potions.get(i);
            if (potion == null || !POTION_ID.equals(potion.ID)) {
                continue;
            }
            if (AbstractDungeon.player.gold <= 0) {
                AbstractDungeon.topPanel.destroyPotion(i);
                continue;
            }
            potion.flash();
            AbstractDungeon.player.loseGold(GOLD_LOSS);
            if (AbstractDungeon.player.gold <= 0) {
                AbstractDungeon.topPanel.destroyPotion(i);
            }
        }
    }
}
