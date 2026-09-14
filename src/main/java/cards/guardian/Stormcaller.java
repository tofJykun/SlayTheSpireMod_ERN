package cards.guardian;

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
import powers.HoverPower;
import powers.StormcallerPower;

public class Stormcaller extends CustomCard {
    public static final String ID = "Stormcaller";
    private static final String IMG_PATH = "img/cards/guardian/Stormcaller.png";
    private static final int COST = 1;
    private static final int HOVER = 10;
    private static final int UPGRADE_PLUS_HOVER = 5;
    private static final int STORMCALLER = 1;

    public Stormcaller() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = HOVER;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new HoverPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StormcallerPower((AbstractCreature)p, STORMCALLER), STORMCALLER));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stormcaller();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HOVER);
            initializeDescription();
        }
    }
}
