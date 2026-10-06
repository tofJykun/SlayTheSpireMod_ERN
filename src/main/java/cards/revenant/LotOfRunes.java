package cards.revenant;

import basemod.abstracts.CustomCard;
import actions.GainGoldWithAnimationAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.ArcanePower;
import powers.LotOfRunesPower;

public class LotOfRunes extends CustomCard {
    public static final String ID = "LotOfRunes";
    private static final String IMG_PATH = "img/cards/revenant/LotOfRunes.png";
    private static final int ARCANE = 6;
    public LotOfRunes() { super(ID, strings().NAME, IMG_PATH, -2, strings().DESCRIPTION, CardType.SKILL,
            CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF); this.exhaust=true; this.baseMagicNumber=50; this.magicNumber=50; }
    private static CardStrings strings(){return CardCrawlGame.languagePack.getCardStrings(ID);}
    @Override public void use(AbstractPlayer p, AbstractMonster m){
        addToBot(new GainGoldWithAnimationAction(this.magicNumber));
        int greyHealth = getGreyHealth();
        addToBot(new ApplyPowerAction(p,p,new ArcanePower(p, ARCANE), ARCANE));
        addToBot(new ApplyPowerAction(p,p,new LotOfRunesPower(p, greyHealth), greyHealth));
    }
    @Override public AbstractCard makeCopy(){return new LotOfRunes();}
    public int getGreyHealth() { return this.upgraded ? 1 : 2; }
    @Override public void upgrade(){if(!upgraded){upgradeName(); baseMagicNumber=100; magicNumber=100; rawDescription=strings().UPGRADE_DESCRIPTION; initializeDescription();}}
}
