package powers;

import cards.tempcards.MottledPot;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PotentateCookbookPower extends AbstractPower {
    public static final String POWER_ID = "PotentateCookbookPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PotentateCookbookPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    public static boolean isActive() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasPower(POWER_ID);
    }

    @Override
    public void onInitialApplication() {
        refreshMottledPots();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = -1;
        refreshMottledPots();
    }

    public static void refreshMottledPots() {
        if (AbstractDungeon.player == null) {
            return;
        }
        refreshGroup(AbstractDungeon.player.hand);
        refreshGroup(AbstractDungeon.player.drawPile);
        refreshGroup(AbstractDungeon.player.discardPile);
        refreshGroup(AbstractDungeon.player.exhaustPile);
        refreshGroup(AbstractDungeon.player.limbo);
        refreshCard(AbstractDungeon.player.cardInUse);
    }

    private static void refreshGroup(CardGroup group) {
        if (group == null) {
            return;
        }
        for (AbstractCard card : group.group) {
            refreshCard(card);
        }
    }

    private static void refreshCard(AbstractCard card) {
        if (card instanceof MottledPot) {
            ((MottledPot)card).refreshCookbookStatus();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
