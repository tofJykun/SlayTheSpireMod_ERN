package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class ResistanceToAilmentsPower extends AbstractPower {
    public static final String POWER_ID="ResistanceToAilmentsPower";
    private static final PowerStrings S=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public ResistanceToAilmentsPower(AbstractCreature owner,int amount){name=S.NAME;ID=POWER_ID;this.owner=owner;this.amount=amount;type=PowerType.BUFF;PowerIconHelper.load(this,POWER_ID);updateDescription();}
    @Override public void atStartOfTurn(){if(owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer)((com.megacrit.cardcrawl.characters.AbstractPlayer)owner).energy.use(this.amount);}
    @Override public void updateDescription(){description=S.DESCRIPTIONS[0];}
}
