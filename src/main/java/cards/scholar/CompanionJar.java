package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.CompanionJarPower;

public class CompanionJar extends CustomCard {
    public static final String ID = "CompanionJar";

    public CompanionJar() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/CompanionJar.png", 2, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 13;
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CompanionJarPower(p, magicNumber), magicNumber));
    }

    @Override public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeMagicNumber(7); }
    }

    @Override public AbstractCard makeCopy() { return new CompanionJar(); }
}
