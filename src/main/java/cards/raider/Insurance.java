package cards.raider;

import actions.InsuranceAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Insurance extends CustomCard {
    public static final String ID = "Insurance";
    private static final String IMG_PATH = "img/cards/raider/Insurance.png";

    public Insurance() {
        super(ID, getCardStrings().NAME, IMG_PATH, 1, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new InsuranceAction(p, getCardStrings().EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Insurance();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
