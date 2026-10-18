package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import general.SigilOfBaphometRewards;

public class SigilOfBaphometPower extends AbstractPower {
    public static final String POWER_ID = "SigilOfBaphometPower";

    public SigilOfBaphometPower(AbstractCreature owner) {
        ID = POWER_ID;
        name = CardCrawlGame.languagePack.getPowerStrings(ID).NAME;
        this.owner = owner;
        amount = -1;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) { amount = -1; }

    @Override
    public void onVictory() { SigilOfBaphometRewards.mark(CombatState.currentRoom()); }

    @Override
    public void updateDescription() {
        description = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS[0];
    }
}
