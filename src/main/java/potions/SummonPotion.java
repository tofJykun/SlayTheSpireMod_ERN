package potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import powers.AbstractSummonPower;
import powers.SummonAsimi;
import powers.SummonDoggo;
import powers.SummonFrederick;
import powers.SummonHelen;
import powers.SummonSebastian;

public class SummonPotion extends AbstractPotion {
    public static final String POTION_ID = "SummonPotion";
    private static final PotionStrings STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public SummonPotion() {
        super(STRINGS.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.WHITE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = STRINGS.DESCRIPTIONS[potency > 1 ? 1 : 0];
        tips.clear();
        tips.add(new PowerTip(name, description));
        String key = description.contains("召唤") ? "召唤" : "summon";
        String body = GameDictionary.keywords.get(key);
        if (body != null) tips.add(new PowerTip(key.equals("summon") ? "Summon" : key, body));
    }

    @Override
    public void use(AbstractCreature target) {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) return;
        AbstractSummonPower chosen;
        switch (AbstractDungeon.cardRandomRng.random(4)) {
            case 0: chosen = new SummonHelen(AbstractDungeon.player); break;
            case 1: chosen = new SummonFrederick(AbstractDungeon.player); break;
            case 2: chosen = new SummonSebastian(AbstractDungeon.player); break;
            case 3: chosen = new SummonDoggo(AbstractDungeon.player); break;
            default: chosen = new SummonAsimi(AbstractDungeon.player); break;
        }
        for (int i = 0; i < potency; i++) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    chosen.makeSummonCopy(AbstractDungeon.player), 1));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) { return 1; }

    @Override
    public AbstractPotion makeCopy() { return new SummonPotion(); }
}
