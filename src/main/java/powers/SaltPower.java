package powers;

import cards.scholar.Salt;
import cards.tempcards.MottledPot;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SaltPower extends AbstractPower {
    public static final String POWER_ID = "SaltPower";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SaltPower(AbstractCreature owner) {
        ID = POWER_ID;
        name = STRINGS.NAME;
        this.owner = owner;
        amount = Salt.MAX_HP_GAIN;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {}

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0 || !MottledPot.ID.equals(card.cardID)) return;
        // Resolve on play so a lethal pot cannot clear a queued Max HP reward.
        amount--;
        flash();
        updateDescription();
        owner.increaseMaxHp(1, true);
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0] + amount + STRINGS.DESCRIPTIONS[1];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color color) {
        if (amount == 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "0", x, y, fontScale, color);
        } else {
            super.renderAmount(sb, x, y, color);
        }
    }
}
