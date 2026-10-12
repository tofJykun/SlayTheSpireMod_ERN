package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;

public class PaperBag extends CustomCard {
    public static final String ID = "PaperBag";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public PaperBag() {
        super(ID, STRINGS.NAME, "img/cards/duchess/PaperBag.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        FragileField.setBase(this, 3);
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new DrawCardAction(magicNumber)); }
    @Override public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeMagicNumber(1); }
    }
    @Override public AbstractCard makeCopy() { return new PaperBag(); }
}
