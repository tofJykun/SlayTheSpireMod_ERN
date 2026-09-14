package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CrudeDrug;

public class DrawstringPotPower extends AbstractPower {
    public static final String POWER_ID = "DrawstringPotPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DrawstringPotPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = -1;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer || AbstractDungeon.player == null) {
            return;
        }

        boolean retainedAny = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (CrudeDrug.isCrudeDrugCard(card) && !card.isEthereal) {
                card.retain = true;
                retainedAny = true;
            }
        }

        if (retainedAny) {
            flash();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
