package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.AbstractCrudeDrugPower;

public class RowaFruitPower extends AbstractCrudeDrugPower {
    public static final String POWER_ID = "RowaFruitPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RowaFruitPower(AbstractCreature owner) {
        super(owner);
        this.name = NAME;
        this.ID = POWER_ID;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static void applyDrugEffect(AbstractPlayer player, AbstractMonster monster) {
        AbstractDungeon.actionManager.addToBottom(
                new ExhaustAction(1, false, false, false));
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(player, 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
