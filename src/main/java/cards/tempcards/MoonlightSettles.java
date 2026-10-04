package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MoonlightSettles extends CustomCard {
    public static final String ID = "MoonlightSettles";
    private static final String IMG_PATH = "img/cards/tempcards/MoonlightSettles.png";

    public MoonlightSettles() {
        this(0);
    }

    public MoonlightSettles(int energy) {
        super(ID, strings().NAME, IMG_PATH, 0, strings().DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.exhaust = true;
        this.selfRetain = true;
        setX(energy);
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public void setX(int energy) {
        this.baseMagicNumber = Math.max(0, energy);
        this.magicNumber = this.baseMagicNumber;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MoonlightSettles(this.baseMagicNumber);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.baseMagicNumber = this.baseMagicNumber;
        copy.magicNumber = this.magicNumber;
        copy.initializeDescription();
        return copy;
    }
}
