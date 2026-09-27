package cards.undertaker;

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
import powers.FilterPower;

public class Filter extends CustomCard {
    public static final String ID = "Filter";
    private static final String IMG_PATH = "img/cards/undertaker/Filter.png";
    private static final int COST = 1;
    private static final int DRAW = 3;
    private static final int DISCARDS = 1;

    public Filter() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME, IMG_PATH, COST,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON,
                CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new FilterPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Filter();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        refreshDescription();
    }

    private void refreshDescription() {
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        String description = cardStrings.DESCRIPTION
                .replace("!M!", Integer.toString(this.magicNumber))
                .replace("!MS!", Integer.toString(DISCARDS));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            refreshDescription();
        }
    }
}
