package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.CatarinaRoundMoonPower;

public class CatarinaRoundMoon extends CustomCard {
    public static final String ID = "CatarinaRoundMoon";
    private static final String IMG_PATH = "img/cards/raider/CatarinaRoundMoon.png";

    public CatarinaRoundMoon() {
        super(ID, getCardStrings().NAME, IMG_PATH, 3, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CatarinaRoundMoonPower(p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CatarinaRoundMoon();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }
}
