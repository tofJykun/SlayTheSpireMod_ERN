package potions;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.DeathScreen;
import general.CombatState;

public class DevilFruit extends AbstractPotion {
    public static final String POTION_ID = "DevilFruit";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int MAX_HP_LOSS = 1;

    public DevilFruit() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.MOON, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], MAX_HP_LOSS, potency, potency);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return;
        }
        AbstractPlayer player = AbstractDungeon.player;
        if (player.isDead) {
            return;
        }
        if (player.maxHealth <= MAX_HP_LOSS) {
            player.maxHealth = 0;
            player.currentHealth = 0;
            player.isDead = true;
            player.healthBarUpdatedEvent();
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            return;
        }
        player.decreaseMaxHealth(MAX_HP_LOSS);
        addToBot(new GainEnergyAction(potency));
        addToBot(new DrawCardAction(player, potency));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DevilFruit();
    }
}
