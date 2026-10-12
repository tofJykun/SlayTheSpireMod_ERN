package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.EdgeStatePower;

public class EdgeState extends CustomCard {
    public static final String ID = "EdgeState";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public EdgeState() {
        super(ID, STRINGS.NAME, "img/cards/duchess/EdgeState.png", 3, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EdgeStatePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EdgeState();
    }
}
