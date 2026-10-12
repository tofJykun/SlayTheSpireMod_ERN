package cards.duchess;

import actions.IgnoreAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Ignore extends CustomCard {
    public static final String ID = "Ignore";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Ignore() {
        super(ID, STRINGS.NAME, "img/cards/duchess/Ignore.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new IgnoreAction(p, STRINGS.EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ignore();
    }
}
