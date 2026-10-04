package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ParryPower;
import powers.PlatingPower;

public class InTheZone extends CustomCard {
    public static final String ID = "InTheZone";

    public InTheZone() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/wylder/InTheZone.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 99;
        exhaust = true;
    }

    public int getPlating() { return upgraded ? 8 : 5; }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ParryPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new PlatingPower(p, getPlating()), getPlating()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new InTheZone(); }
}
