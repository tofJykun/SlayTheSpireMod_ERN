package powers;

import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;

public class EarthSeekerPower extends AbstractPower {
    public static final String POWER_ID = "EarthSeekerPower";
    private static final String CARD_ID = "EarthSeeker";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EarthSeekerPower(AbstractCreature owner) {
        this.name = POWER_STRINGS.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.exhaustPile == null
                || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }

        ArrayList<AbstractCard> cardsToPlay = new ArrayList<AbstractCard>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (isEarthSeeker(card)) {
                cardsToPlay.add(card);
            }
        }
        for (AbstractCard card : cardsToPlay) {
            playFromExhaust(card);
        }
    }

    private static boolean isEarthSeeker(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (CARD_ID.equals(card.cardID)) {
            return true;
        }
        if (card instanceof CraftmanCreation) {
            AbstractCard body = ((CraftmanCreation)card).getBodyCardCopy();
            return body != null && CARD_ID.equals(body.cardID);
        }
        return false;
    }

    private static void playFromExhaust(AbstractCard card) {
        AbstractDungeon.player.exhaustPile.group.remove(card);
        AbstractDungeon.player.limbo.addToBottom(card);
        card.current_x = AbstractDungeon.player.hb.cX;
        card.current_y = AbstractDungeon.player.hb.cY;
        card.target_x = AbstractDungeon.player.hb.cX;
        card.target_y = AbstractDungeon.player.hb.cY;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.freeToPlayOnce = true;
        card.isInAutoplay = true;
        card.applyPowers();
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, true,
                EnergyPanel.getCurrentEnergy(), true, true), true);
    }

    @Override
    public void updateDescription() {
        this.description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
