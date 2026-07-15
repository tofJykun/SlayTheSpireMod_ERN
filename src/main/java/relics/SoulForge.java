package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.AbstractSummonPower;

public class SoulForge extends CustomRelic {
    public static final String ID = "SoulForge";
    private static final String IMG = "img/relics/revenant/SoulForge.png";
    private static final String IMG_OTL = "img/relics/revenant/outline/SoulForge.png";
    private static final int ENERGY = 1;

    public SoulForge() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (AbstractDungeon.player != null
                                && AbstractSummonPower.hasSummonPower(
                                (AbstractCreature)AbstractDungeon.player)) {
                            SoulForge.this.flash();
                            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(ENERGY));
                            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(
                                    (AbstractCreature)AbstractDungeon.player, SoulForge.this));
                        }
                        this.isDone = true;
                    }
                });
                this.isDone = true;
            }
        });
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulForge();
    }
}
