package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.DuskHerbDewPower;

public class DuskHerbDew extends AbstractPotion {
    public static final String POTION_ID = "DuskHerbDew";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_BLOCK = 5;

    public DuskHerbDew() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.POWER);
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
        AbstractPlayer player = AbstractDungeon.player;
        if (CombatState.isInCombat() && player != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)player,
                    (AbstractCreature)player, new DuskHerbDewPower((AbstractCreature)player, this.potency),
                    this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_BLOCK;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DuskHerbDew();
    }
}
