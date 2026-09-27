package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.InsightPower;

public class GreatStrength extends CustomCard {
    public static final String ID = "GreatStrength";
    private static final String IMG_PATH = "img/cards/undertaker/GreatStrength.png";
    public GreatStrength() { super(ID, strings().NAME, IMG_PATH, -2, strings().DESCRIPTION, CardType.SKILL,
            CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF); this.exhaust=true; this.baseMagicNumber=4; this.magicNumber=4; }
    private static CardStrings strings(){return CardCrawlGame.languagePack.getCardStrings(ID);}
    @Override public void use(AbstractPlayer p, AbstractMonster m){
        addToBot(new ApplyPowerAction(p,p,new StrengthPower(p,this.magicNumber),this.magicNumber));
        addToBot(new ApplyPowerAction(p,p,new DexterityPower(p,-1),-1));
        addToBot(new ApplyPowerAction(p,p,new InsightPower(p,-1),-1));
    }
    @Override public AbstractCard makeCopy(){return new GreatStrength();}
    @Override public void upgrade(){if(!upgraded){upgradeName();upgradeMagicNumber(2);}}
}
