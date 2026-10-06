package relics;

import actions.TripleBurrowsAction;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import general.FinalPlanHelper;

public class TripleBurrows extends CustomRelic {
    public static final String ID = "TripleBurrows";
    private boolean triggeredThisCombat;

    public TripleBurrows() {
        super(ID, ImageMaster.loadImage("img/relics/revenant/TripleBurrows.png"),
                ImageMaster.loadImage("img/relics/revenant/outline/TripleBurrows.png"),
                RelicTier.RARE, LandingSound.CLINK);
        String key = description.contains("锦囊") ? "锦囊" : "final plan";
        String body = GameDictionary.keywords.get(key);
        if (body != null && tips.stream().noneMatch(t -> key.equalsIgnoreCase(t.header))) {
            tips.add(new PowerTip(key.equals("final plan") ? "Final Plan" : key, body));
        }
    }

    @Override
    public void atBattleStart() {
        triggeredThisCombat = false;
        grayscale = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (triggeredThisCombat || !FinalPlanHelper.isFinalPlanCard(card) || AbstractDungeon.player == null) return;
        triggeredThisCombat = true;
        grayscale = true;
        flash();
        for (int i = 0; i < 2; i++) {
            AbstractCard copy = card.makeSameInstanceOf();
            copy.energyOnUse = card.energyOnUse;
            AbstractDungeon.actionManager.addToBottom(new TripleBurrowsAction(copy, monster));
        }
    }

    @Override
    public void onVictory() {
        grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TripleBurrows();
    }
}
