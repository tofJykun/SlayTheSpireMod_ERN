package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.RegenPower;
import general.CombatState;

public class CrimsonPotion extends AbstractPotion {
    public static final String POTION_ID = "CrimsonPotion";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int BASE_REGEN = 3;
    private static final int BASE_ENERGY = 1;
    private int energyAmount;

    public CrimsonPotion() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.energyAmount = this.potency / BASE_REGEN * BASE_ENERGY;
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency
                + POTION_STRINGS.DESCRIPTIONS[1] + this.energyAmount
                + POTION_STRINGS.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.REGEN.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.REGEN.NAMES[0])));
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractPlayer player = AbstractDungeon.player;
        if (CombatState.isInCombat() && player != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)player,
                    (AbstractCreature)player, new RegenPower((AbstractCreature)player, this.potency),
                    this.potency));
            addToBot((AbstractGameAction)new GainEnergyAction(this.energyAmount));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_REGEN;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new CrimsonPotion();
    }
}
