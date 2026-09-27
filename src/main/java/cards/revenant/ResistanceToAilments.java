package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import patches.AbstractCardEnum;
import powers.ResistanceToAilmentsPower;
import java.util.ArrayList;

public class ResistanceToAilments extends CustomCard {
    public static final String ID = "ResistanceToAilments";
    private static final String IMG_PATH = "img/cards/revenant/ResistanceToAilments.png";
    public ResistanceToAilments() { super(ID, strings().NAME, IMG_PATH, -2, strings().DESCRIPTION, CardType.SKILL,
            CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF); this.exhaust = true; this.baseMagicNumber=99; this.magicNumber=99; }
    private static CardStrings strings() { return CardCrawlGame.languagePack.getCardStrings(ID); }
    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractPower> remove = new ArrayList<>();
        for (AbstractPower power : p.powers) if (power.type == AbstractPower.PowerType.DEBUFF || ArtifactPower.POWER_ID.equals(power.ID)) remove.add(power);
        for (AbstractPower power : remove) addToBot(new RemoveSpecificPowerAction(p, p, power.ID));
        if (upgraded) {
            p.energy.use(1);
        } else {
            addToBot(new ApplyPowerAction(p, p, new ResistanceToAilmentsPower(p, 1), 1));
        }
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, this.magicNumber), this.magicNumber));
    }
    @Override public AbstractCard makeCopy() { return new ResistanceToAilments(); }
    @Override public void upgrade() { if(!upgraded){upgradeName(); rawDescription=strings().UPGRADE_DESCRIPTION; initializeDescription();} }
}
