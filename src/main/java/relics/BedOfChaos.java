package relics;

import basemod.abstracts.CustomRelic;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.BedOfMagicCostReductionPower;
import powers.BedOfMagicPowerTriggerPower;

public class BedOfChaos extends CustomRelic {
    public static final String ID = "BedOfChaos";
    private static final String IMG = "img/relics/recluse/BedOfChaos.png";
    private static final String IMG_OTL = "img/relics/recluse/outline/BedOfChaos.png";
    private static final int EFFECT_COUNT = 3;

    public BedOfChaos() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public void bossObtainLogic() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicCocktail.ID)) {
            instantObtain(AbstractDungeon.player, 0, true);
        } else {
            instantObtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicCocktail.ID);
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player == null) {
            return;
        }

        flash();
        AbstractDungeon.actionManager.addToBottom(
                new RelicAboveCreatureAction(AbstractDungeon.player, this));
        switch (AbstractDungeon.cardRandomRng.random(3)) {
            case 0:
                applyCostReduction(AbstractCard.CardType.SKILL);
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                        (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                        new BedOfMagicPowerTriggerPower((AbstractCreature)AbstractDungeon.player, EFFECT_COUNT),
                        EFFECT_COUNT));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(
                        (AbstractGameAction)new MakeTempCardInHandAction(new FadingPrimalGlintstone(), 1));
                break;
            default:
                applyCostReduction(AbstractCard.CardType.ATTACK);
                break;
        }
    }

    private void applyCostReduction(AbstractCard.CardType cardType) {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                new BedOfMagicCostReductionPower((AbstractCreature)AbstractDungeon.player, cardType, EFFECT_COUNT),
                EFFECT_COUNT));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BedOfChaos();
    }
}
