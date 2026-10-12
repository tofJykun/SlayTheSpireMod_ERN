package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.MushroomPower;
import powers.MushroomSoupPower;
import java.util.Locale;

public class MushroomSoup extends AbstractPotion {
    public static final String POTION_ID = "MushroomSoup";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public MushroomSoup() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    private int cardsRequired() {
        return Math.max(1, 2 / Math.max(1, potency));
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = String.format(STRINGS.DESCRIPTIONS[0], cardsRequired());
        tips.clear();
        tips.add(new PowerTip(name, description));
        String mushroomTip = GameDictionary.keywords.get(MushroomPower.NAME.toLowerCase(Locale.ROOT));
        tips.add(new PowerTip(MushroomPower.NAME,
                mushroomTip != null ? mushroomTip : STRINGS.DESCRIPTIONS[1]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new MushroomSoupPower(AbstractDungeon.player, cardsRequired()), 1));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new MushroomSoup();
    }
}
