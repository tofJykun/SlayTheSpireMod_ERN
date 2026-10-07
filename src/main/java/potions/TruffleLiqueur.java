package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import general.CombatState;
import powers.BrightbugPower;

public class TruffleLiqueur extends AbstractPotion {
    public static final String POTION_ID = "TruffleLiqueur";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final PowerStrings VIGOR_STRINGS = CardCrawlGame.languagePack.getPowerStrings(VigorPower.POWER_ID);
    private static final int BASE_VIGOR = 4;

    public TruffleLiqueur() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency + POTION_STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(VIGOR_STRINGS.NAME,
                VIGOR_STRINGS.DESCRIPTIONS[0] + this.potency + VIGOR_STRINGS.DESCRIPTIONS[1]));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new BrightbugPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_VIGOR;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TruffleLiqueur();
    }
}
