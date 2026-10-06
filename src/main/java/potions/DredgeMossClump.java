package potions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class DredgeMossClump extends AbstractPotion {
    public static final String POTION_ID = "DredgeMossClump";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int REMOVAL_PERCENT = 50;

    public DredgeMossClump() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.RARE, PotionSize.S, PotionColor.GREEN);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(GreyHealthPower.NAME, GreyHealthPower.DESCRIPTIONS[0]));
        this.tips.add(new PowerTip(GreyHealthPlusPower.NAME, GreyHealthPlusPower.DESCRIPTIONS[0]));
    }

    @Override
    public void use(AbstractCreature target) {
        reduceGreyHealth(GreyHealthPower.POWER_ID);
        reduceGreyHealth(GreyHealthPlusPower.POWER_ID);
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return REMOVAL_PERCENT;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DredgeMossClump();
    }

    private void reduceGreyHealth(String powerId) {
        if (AbstractDungeon.player == null) {
            return;
        }
        AbstractPower power = AbstractDungeon.player.getPower(powerId);
        if (power == null || power.amount <= 0) {
            return;
        }
        int amountToRemove = MathUtils.ceil(power.amount * this.potency / 100.0F);
        addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature)AbstractDungeon.player,
                (AbstractCreature)AbstractDungeon.player, power, amountToRemove));
    }
}
