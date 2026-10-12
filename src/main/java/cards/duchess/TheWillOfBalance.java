package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.TheWillOfBalancePower;

public class TheWillOfBalance extends CustomCard {
    public static final String ID = "TheWillOfBalance";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public TheWillOfBalance() {
        super(ID, STRINGS.NAME, "img/cards/duchess/TheWillOfBalance.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public int getStatGain() { return this.upgraded ? 5 : 4; }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TheWillOfBalancePower(p, getStatGain()), getStatGain()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new TheWillOfBalance(); }
}
