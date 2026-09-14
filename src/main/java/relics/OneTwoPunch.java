package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.OneTwoPunchPatch;

public class OneTwoPunch extends CustomRelic {
    public static final String ID = "OneTwoPunch";
    private static final String IMG = "img/relics/raider/OneTwoPunch.png";
    private static final String IMG_OTL = "img/relics/raider/outline/OneTwoPunch.png";

    public OneTwoPunch() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.SHOP, LandingSound.FLAT);
        this.counter = -1;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        this.grayscale = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.counter != 0 || card == null || card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        this.counter = 1;
        this.grayscale = true;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        OneTwoPunchPatch.Fields.returnAfterUse.set(action, true);
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OneTwoPunch();
    }
}
