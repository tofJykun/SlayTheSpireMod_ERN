package powers;

import cards.tempcards.PhantomFrederick;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonFrederick extends AbstractSummonPower {
    public static final String POWER_ID = "SummonFrederick";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK = 8;
    public static final String SUMMON_KEY = "Frederick";

    public SummonFrederick(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings.NAME, powerStrings.DESCRIPTIONS, BLOCK, new PhantomFrederick(), SUMMON_KEY);
    }
}
