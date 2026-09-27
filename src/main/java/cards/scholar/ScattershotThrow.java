package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ScattershotThrowPower;

public class ScattershotThrow extends CustomCard {
    public static final String ID = "ScattershotThrow";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public ScattershotThrow() {
        super(ID, STRINGS.NAME, "img/cards/scholar/ScattershotThrow.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ScattershotThrowPower(p, magicNumber - 1), magicNumber - 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScattershotThrow();
    }
}
