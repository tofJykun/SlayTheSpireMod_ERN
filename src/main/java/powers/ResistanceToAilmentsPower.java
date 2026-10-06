package powers;

import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ResistanceToAilmentsPower extends AbstractPower {
    public static final String POWER_ID="ResistanceToAilmentsPower";
    private static final PowerStrings S=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public ResistanceToAilmentsPower(AbstractCreature owner,int amount){name=S.NAME;ID=POWER_ID;this.owner=owner;this.amount=amount;type=PowerType.BUFF;PowerIconHelper.load(this,POWER_ID);updateDescription();}
    @Override
    public void atStartOfTurn() {
        if (owner instanceof AbstractPlayer) {
            flash();
            // Turn energy recharges after start-of-turn hooks, before queued actions.
            addToBot(new LoseEnergyAction(this.amount));
        }
    }
    @Override public void updateDescription(){description=S.DESCRIPTIONS[0];}
}
