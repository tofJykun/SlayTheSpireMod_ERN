package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class BulkBlockPotion extends AbstractPotion {
    public static final String POTION_ID = "BulkBlockPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BulkBlockPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.BLUE);
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1]
                + potionStrings.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.BLOCK.NAMES[0])));
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot((AbstractGameAction)new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.potency));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 12;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BulkBlockPotion();
    }
}
