package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.AbstractCrudeDrugPower;

import java.util.ArrayList;

public class SacramentalBudPower extends AbstractCrudeDrugPower {
    public static final String POWER_ID = "SacramentalBudPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SacramentalBudPower(AbstractCreature owner) {
        super(owner);
        this.name = NAME;
        this.ID = POWER_ID;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static void applyDrugEffect(AbstractPlayer player, AbstractMonster monster) {
        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        for (AbstractPower power : player.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs.add(power);
            }
        }
        for (AbstractPower power : debuffs) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveSpecificPowerAction(player, player, power.ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
