package cards.scholar;

import actions.SigilOfBaphometAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SigilOfBaphometPower;

public class SigilOfBaphomet extends CustomCard {
    public static final String ID = "SigilOfBaphomet";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SigilOfBaphomet() {
        super(ID, STRINGS.NAME, "img/cards/scholar/SigilOfBaphomet.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SigilOfBaphometPower(p), 1));
        addToBot(new SigilOfBaphometAction(STRINGS.EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override
    public AbstractCard makeCopy() { return new SigilOfBaphomet(); }
}
