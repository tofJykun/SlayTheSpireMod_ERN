package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.FinalPlanHelper;

public class TrialSizePackaging extends CustomRelic {
    public static final String ID = "TrialSizePackaging";
    private boolean triggeredThisTurn;

    public TrialSizePackaging() {
        super(ID, ImageMaster.loadImage("img/relics/revenant/TrialSizePackaging.png"),
                ImageMaster.loadImage("img/relics/revenant/outline/TrialSizePackaging.png"),
                RelicTier.BOSS, LandingSound.CLINK);
        String key = description.contains("锦囊") ? "锦囊" : "final plan";
        String body = GameDictionary.keywords.get(key);
        if (body != null && tips.stream().noneMatch(t -> key.equalsIgnoreCase(t.header))) {
            tips.add(new PowerTip(key.equals("final plan") ? "Final Plan" : key, body));
        }
    }

    @Override
    public void atBattleStart() {
        triggeredThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        triggeredThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (triggeredThisTurn || !FinalPlanHelper.isFinalPlanCard(card) || AbstractDungeon.player == null) return;
        triggeredThisTurn = true;
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 2));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TrialSizePackaging();
    }
}
