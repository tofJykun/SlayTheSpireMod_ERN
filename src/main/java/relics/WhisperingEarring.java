package relics;

import actions.RandomPlayHelper;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CombatState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WhisperingEarring extends CustomRelic {
    public static final String ID = "WhisperingEarring";
    private static final String IMG = "img/relics/undertaker/WhisperingEarring.png";
    private static final String IMG_OTL = "img/relics/undertaker/outline/WhisperingEarring.png";
    private static final int ENERGY = 1;
    private boolean usedThisTurn = false;
    private final Map<AbstractCard, AbstractCard.CardTarget> originalTargets = new HashMap<>();

    public WhisperingEarring() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.usedThisTurn = false;
        refreshHandTargets();
    }

    @Override
    public void atTurnStart() {
        this.usedThisTurn = false;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(
                (AbstractCreature)AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
        refreshHandTargets();
    }

    @Override
    public void atTurnStartPostDraw() {
        refreshHandTargets();
    }

    @Override
    public void onDrawOrDiscard() {
        refreshHandTargets();
    }

    @Override
    public void onRefreshHand() {
        refreshHandTargets();
    }

    @Override
    public void update() {
        super.update();
        if (isInCombat()) {
            refreshHandTargets();
        }
    }

    public boolean shouldRandomize(AbstractCard card) {
        return !this.usedThisTurn && (this.originalTargets.containsKey(card) || isDirectedCard(card));
    }

    public void consume(AbstractCard card) {
        this.usedThisTurn = true;
        restoreCardTarget(card);
        restoreAllTargets();
        flash();
        RandomPlayHelper.notifyRandomCardPlayed();
    }

    public void consumeInsightChoice() {
        RandomPlayHelper.consumeInsightPrompt(AbstractDungeon.player);
    }

    private void refreshHandTargets() {
        if (!isInCombat() || AbstractDungeon.player == null) {
            restoreAllTargets();
            return;
        }

        if (this.usedThisTurn || RandomPlayHelper.shouldChooseRandomPlay(AbstractDungeon.player)) {
            restoreAllTargets();
            return;
        }

        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (isDirectedCard(card)) {
                this.originalTargets.put(card, card.target);
                card.target = AbstractCard.CardTarget.NONE;
            }
        }
        restoreTargetsNoLongerInHand();
    }

    private void restoreTargetsNoLongerInHand() {
        Iterator<Map.Entry<AbstractCard, AbstractCard.CardTarget>> iterator = this.originalTargets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AbstractCard, AbstractCard.CardTarget> entry = iterator.next();
            if (AbstractDungeon.player == null || !AbstractDungeon.player.hand.group.contains(entry.getKey())) {
                entry.getKey().target = entry.getValue();
                iterator.remove();
            }
        }
    }

    private void restoreCardTarget(AbstractCard card) {
        AbstractCard.CardTarget originalTarget = this.originalTargets.remove(card);
        if (originalTarget != null) {
            card.target = originalTarget;
        }
    }

    private void restoreAllTargets() {
        for (Map.Entry<AbstractCard, AbstractCard.CardTarget> entry : this.originalTargets.entrySet()) {
            entry.getKey().target = entry.getValue();
        }
        this.originalTargets.clear();
    }

    private static boolean isDirectedCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        return card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
    }

    private static boolean isInCombat() {
        return CombatState.isInCombat();
    }

    @Override
    public void onVictory() {
        restoreAllTargets();
        this.usedThisTurn = true;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WhisperingEarring();
    }
}
