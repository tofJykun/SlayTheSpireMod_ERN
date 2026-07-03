package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.ElementalDefensePower;

public class BequestOfSeath extends CustomRelic {
    public static final String ID = "BequestOfSeath";
    private static final String IMG = "img/relics/recluse/BequestOfSeath.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/BequestOfSeath.png";
    private boolean triggeredThisTurn = false;

    public BequestOfSeath() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.triggeredThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        this.triggeredThisTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.triggeredThisTurn && ElementalDefensePower.isMagicCard(card)) {
            this.triggeredThisTurn = true;
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
    }

    @Override
    public void onVictory() {
        this.triggeredThisTurn = false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BequestOfSeath();
    }
}
