package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.UsuryPower;
import powers.VirtualCurrencyPower;

public class Usury extends CustomCard {
    public static final String ID = "Usury";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/Usury.png";
    private static final int COST = 1;
    private static final int TOKEN_DIVISOR = 4;
    private static final int UPGRADE_TOKEN_GAIN = 10;
    private static final int POWER_AMOUNT = 1;

    public Usury() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = UPGRADE_TOKEN_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    public int getTokenDivisor() {
        return TOKEN_DIVISOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new VirtualCurrencyPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new UsuryPower((AbstractCreature)p, POWER_AMOUNT), POWER_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Usury();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
