package potions;

import actions.ClockwiseSequencer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;

public class SeafoodFeast extends AbstractPotion {
    public static final String POTION_ID = "SeafoodFeast";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_PLAYS = 3;

    public SeafoodFeast() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.S, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency + POTION_STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            ClockwiseSequencer.requestRight(this.potency);
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_PLAYS;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SeafoodFeast();
    }
}
