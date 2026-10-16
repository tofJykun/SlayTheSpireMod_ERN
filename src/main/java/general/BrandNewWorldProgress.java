package general;

import basemod.abstracts.CustomSavable;
import cards.scholar.BrandNewWorld;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.EnumSet;

public final class BrandNewWorldProgress implements CustomSavable<Boolean> {
    public static final String SAVE_KEY = "ERN:BrandNewWorldEarned";
    private static final EnumSet<CardElement> forwardElements = EnumSet.noneOf(CardElement.class);
    private static boolean earned;

    public static void resetRun() { earned = false; resetCombat(); }
    public static void resetCombat() { forwardElements.clear(); }

    public static void onForward(CardElement element) {
        if (earned || element == null || AbstractDungeon.player == null) return;
        forwardElements.add(element);
        if (forwardElements.size() != CardElement.values().length) return;
        // Reserve the one-time reward before obtaining it; victory may clear the action queue.
        earned = true;
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new BrandNewWorld(),
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX,
                AbstractDungeon.player.dialogY, 4.0F,
                CardCrawlGame.languagePack.getCardStrings(BrandNewWorld.ID).EXTENDED_DESCRIPTION[0], true));
    }

    @Override
    public Boolean onSave() { return earned; }

    @Override
    public void onLoad(Boolean saved) {
        earned = Boolean.TRUE.equals(saved);
        resetCombat();
    }
}
