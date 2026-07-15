package powers;

import cards.tempcards.PhantomDoggo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonDoggo extends AbstractSummonPower {
    public static final String POWER_ID = "SummonDoggo";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK = 2;
    public static final String SUMMON_KEY = "Doggo";

    public SummonDoggo(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings.NAME, powerStrings.DESCRIPTIONS, BLOCK, new PhantomDoggo(), SUMMON_KEY);
    }
}
