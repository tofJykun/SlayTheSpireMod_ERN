package potions;

import basemod.BaseMod;
import basemod.helpers.CardPowerTip;
import cards.wylder.PitaBread;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;

public class BagOfRolls extends AbstractPotion {
    public static final String POTION_ID = "BagOfRolls";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BagOfRolls() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.JAR, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        PitaBread preview = new PitaBread();
        if (potency > 1) preview.upgrade();
        description = STRINGS.DESCRIPTIONS[potency > 1 ? 1 : 0];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(preview));
    }

    @Override
    public void use(AbstractCreature target) {
        if (CombatState.isInCombat() && AbstractDungeon.player != null) {
            final boolean upgraded = potency > 1;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    int count = Math.max(0, BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size());
                    if (count > 0) {
                        PitaBread bread = new PitaBread();
                        if (upgraded) bread.upgrade();
                        addToTop(new MakeTempCardInHandAction(bread, count));
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public int getPotency(int ascensionLevel) { return 1; }

    @Override
    public AbstractPotion makeCopy() { return new BagOfRolls(); }
}
