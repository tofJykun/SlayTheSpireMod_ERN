package cards.ironeye;

import actions.StarsDiceAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.VirtualCurrencyPower;

public class HighFrequency extends CustomCard {
    public static final String ID = "HighFrequency";
    public static final int TOKEN_GAIN = 10;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public HighFrequency() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/HighFrequency.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VirtualCurrencyPower(p, TOKEN_GAIN), TOKEN_GAIN));
        addToBot(new StarsDiceAction(magicNumber, true));
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
    public AbstractCard makeCopy() { return new HighFrequency(); }
}
