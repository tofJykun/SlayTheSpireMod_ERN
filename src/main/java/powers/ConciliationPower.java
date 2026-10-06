package powers;

import actions.ConciliationAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.CombatState;

public class ConciliationPower extends AbstractPower {
    public static final String POWER_ID = "ConciliationPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractRoom room;
    private final int threshold;

    public ConciliationPower(AbstractCreature owner, AbstractRoom room, int threshold) {
        this.ID = POWER_ID;
        this.name = STRINGS.NAME;
        this.owner = owner;
        this.room = room;
        this.threshold = Math.max(1, threshold);
        this.amount = 0;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // A second copy neither changes the target nor resets progress.
        flash();
    }

    public void onSummon() {
        if (amount >= threshold || CombatState.currentRoom() != room
                || !ConciliationAction.isEligible(room)) return;
        amount++;
        fontScale = 8.0F;
        updateDescription();
        if (amount == threshold) {
            flash();
            addToBot(new ConciliationAction(room));
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color color) {
        if (amount == 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "0", x, y, fontScale, color);
        } else {
            super.renderAmount(sb, x, y, color);
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], threshold, amount);
    }
}
