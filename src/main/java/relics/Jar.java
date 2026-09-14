package relics;

import actions.ReplayCardAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.CrudeDrug;

public class Jar extends CustomRelic {
    public static final String ID = "Jar";
    private static final String IMG = "img/relics/scholar/Jar.png";
    private static final String IMG_OTL = "img/relics/scholar/outline/Jar.png";
    private static final int EXTRA_PLAYS = 2;

    private boolean triggeredThisCombat;

    public Jar() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.BOSS, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.triggeredThisCombat = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.triggeredThisCombat || !CrudeDrug.isCrudeDrugCard(card)) {
            return;
        }
        this.triggeredThisCombat = true;
        flash();
        AbstractCreature target = action == null ? null : action.target;
        AbstractMonster monster = target instanceof AbstractMonster ? (AbstractMonster)target : null;
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ReplayCardAction(card, monster, EXTRA_PLAYS));
    }

    @Override
    public void onVictory() {
        this.triggeredThisCombat = false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Jar();
    }
}
