package cards.revenant;

import actions.BlasphemousBladeAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class BlasphemousBlade extends CustomCard {
    public static final String ID = "BlasphemousBlade";

    public BlasphemousBlade() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/revenant/BlasphemousBlade.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 15;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BlasphemousBladeAction(p, multiDamage, damageTypeForTurn, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(5);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlasphemousBlade();
    }
}
