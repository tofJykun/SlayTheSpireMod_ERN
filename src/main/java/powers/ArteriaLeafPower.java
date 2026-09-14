package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import general.AbstractCrudeDrugPower;

public class ArteriaLeafPower extends AbstractCrudeDrugPower {
    public static final String POWER_ID = "ArteriaLeafPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int STRENGTH = 6;

    public ArteriaLeafPower(AbstractCreature owner) {
        super(owner);
        this.name = NAME;
        this.ID = POWER_ID;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static void applyDrugEffect(AbstractPlayer player, AbstractMonster monster) {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(player, player,
                new StrengthPower(player, STRENGTH), STRENGTH));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(player, player,
                new LoseStrengthPower(player, STRENGTH), STRENGTH));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
