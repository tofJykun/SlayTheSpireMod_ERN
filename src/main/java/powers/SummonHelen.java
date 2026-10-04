package powers;

import cards.tempcards.PhantomHelen;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonHelen extends AbstractSummonPower {
    public static final String POWER_ID = "SummonHelen";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK = 3;
    public static final String SUMMON_KEY = "Helen";

    public SummonHelen(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings.NAME, powerStrings.DESCRIPTIONS, BLOCK, new PhantomHelen(), SUMMON_KEY);
    }

    @Override
    public AbstractSummonPower makeSummonCopy(AbstractCreature owner) {
        return new SummonHelen(owner);
    }
}
