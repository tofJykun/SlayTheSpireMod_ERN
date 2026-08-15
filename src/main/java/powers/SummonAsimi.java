package powers;

import cards.tempcards.PhantomAsimi;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonAsimi extends AbstractSummonPower {
    public static final String POWER_ID = "SummonAsimi";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK = 5;
    public static final String SUMMON_KEY = "Asimi";

    public SummonAsimi(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings.NAME, powerStrings.DESCRIPTIONS, BLOCK, new PhantomAsimi(), SUMMON_KEY);
    }
}
