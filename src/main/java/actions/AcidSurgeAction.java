package actions;

import cards.raider.AcidSurge;
import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AcidSurgeAction extends AbstractGameAction {
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(AcidSurge.ID);

    private final AbstractPlayer player;
    private final AbstractMonster targetMonster;
    private final int strengthLoss;

    public AcidSurgeAction(AbstractPlayer player, AbstractMonster targetMonster, int strengthLoss) {
        this.player = player;
        this.targetMonster = targetMonster;
        this.strengthLoss = strengthLoss;
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            CardGroup creations = getCraftmanCreations();
            if (this.player == null || creations.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (creations.size() == 1) {
                exhaustAndApply(creations.getTopCard());
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(creations, 1,
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0], false, false, false, false);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            exhaustAndApply(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.isDone = true;
    }

    private CardGroup getCraftmanCreations() {
        CardGroup creations = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.player == null) {
            return creations;
        }
        for (AbstractCard card : this.player.hand.group) {
            if (card instanceof CraftmanCreation) {
                creations.addToTop(card);
            }
        }
        return creations;
    }

    private void exhaustAndApply(AbstractCard card) {
        if (card == null || this.player == null || !this.player.hand.group.contains(card)) {
            return;
        }

        this.player.hand.moveToExhaustPile(card);
        CardCrawlGame.dungeon.checkForPactAchievement();
        this.player.hand.refreshHandLayout();
        if (this.targetMonster != null && !this.targetMonster.isDeadOrEscaped()) {
            addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.targetMonster,
                    (AbstractCreature)this.player,
                    (AbstractPower)new StrengthPower((AbstractCreature)this.targetMonster, -this.strengthLoss),
                    -this.strengthLoss, true, AttackEffect.NONE));
        }
    }
}
