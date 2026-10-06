package powers;

import cards.tempcards.PhantomHelen;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RepeatingCrossbowPower extends AbstractPower {
    public static final String POWER_ID = "RepeatingCrossbowPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int EXTRA_PLAYS = 2;

    public static boolean makesFree(AbstractCard card) {
        return card != null && PhantomHelen.ID.equals(card.cardID)
                && AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(POWER_ID)
                && AbstractSummonPower.isActiveSummon(AbstractDungeon.player, SummonHelen.POWER_ID);
    }

    public RepeatingCrossbowPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        updateDescription();
        PowerIconHelper.load(this, POWER_ID);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = -1;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.purgeOnUse || !PhantomHelen.ID.equals(card.cardID) || !this.owner.hasPower(SummonHelen.POWER_ID)) {
            return;
        }

        flash();
        AbstractMonster m = null;
        if (action.target != null) {
            m = (AbstractMonster)action.target;
        }

        for (int i = 0; i < EXTRA_PLAYS; i++) {
            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

