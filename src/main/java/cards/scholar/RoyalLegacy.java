package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class RoyalLegacy extends CustomCard {
    public static final String ID = "RoyalLegacy";
    private static final String IMG_PATH = "img/cards/scholar/RoyalLegacy.png";
    private static final int COST = 2;
    private static final int GOLD = 35;
    private static final int UPGRADED_GOLD = 45;

    public RoyalLegacy() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = GOLD;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainGoldAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RoyalLegacy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADED_GOLD;
            this.magicNumber = this.baseMagicNumber;
            initializeDescription();
        }
    }
}
