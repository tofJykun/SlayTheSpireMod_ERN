package powers;

import cards.tempcards.PhantomSebastian;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonSebastian extends AbstractSummonPower {
    public static final String POWER_ID = "SummonSebastian";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BLOCK = 5;
    public static final String SUMMON_KEY = "Sebastian";

    public SummonSebastian(AbstractCreature owner) {
        super(owner, POWER_ID, powerStrings.NAME, powerStrings.DESCRIPTIONS, BLOCK, new PhantomSebastian(), SUMMON_KEY);
    }
}
