package actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import powers.InsightPower;
import powers.OuterBoonPower;
import powers.ZealotryPower;

public class RandomPlayHelper {
    private RandomPlayHelper() {
    }

    public static boolean shouldChooseRandomPlay(AbstractPlayer player) {
        if (player == null || !player.hasPower(InsightPower.POWER_ID)) {
            return false;
        }
        InsightPower power = (InsightPower)player.getPower(InsightPower.POWER_ID);
        return power.canChooseRandomPlay();
    }

    public static boolean shouldChooseRandomDiscard(AbstractPlayer player) {
        return shouldChooseRandomPlay(player);
    }

    public static boolean shouldChooseRandomExhaust(AbstractPlayer player) {
        return shouldChooseRandomPlay(player);
    }

    public static String consumeInsightPrompt(AbstractPlayer player) {
        InsightPower power = (InsightPower)player.getPower(InsightPower.POWER_ID);
        power.consumeRandomPlayChoice();
        return power.getPrompt();
    }

    public static String consumeInsightDiscardPrompt(AbstractPlayer player) {
        InsightPower power = (InsightPower)player.getPower(InsightPower.POWER_ID);
        power.consumeRandomPlayChoice();
        return power.getDiscardPrompt();
    }

    public static String consumeInsightExhaustPrompt(AbstractPlayer player) {
        InsightPower power = (InsightPower)player.getPower(InsightPower.POWER_ID);
        power.consumeRandomPlayChoice();
        return power.getExhaustPrompt();
    }

    public static void prepareRandomPlayedCard(AbstractCard card) {
        card = general.SmithingBody.physical(card);
        card.current_x = card.hb.cX;
        card.current_y = card.hb.cY;
        card.target_x = AbstractDungeon.player.hb.cX;
        card.target_y = AbstractDungeon.player.hb.cY;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.freeToPlayOnce = true;
        card.applyPowers();
    }

    public static void notifyRandomCardPlayed() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(OuterBoonPower.POWER_ID)) {
            ((OuterBoonPower)AbstractDungeon.player.getPower(OuterBoonPower.POWER_ID)).onRandomCardPlayed();
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(ZealotryPower.POWER_ID)) {
            ((ZealotryPower)AbstractDungeon.player.getPower(ZealotryPower.POWER_ID)).onRandomCardPlayed();
        }
    }

    public static void notifyRandomCardDiscarded() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(OuterBoonPower.POWER_ID)) {
            ((OuterBoonPower)AbstractDungeon.player.getPower(OuterBoonPower.POWER_ID)).onRandomCardDiscarded();
        }
    }
}
