package cards.revenant;

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
import powers.SummonDoggo;

public class DoggoXD extends CustomCard {
    public static final String ID = "DoggoXD";
    private static final String IMG_PATH = "img/cards/revenant/DoggoXD.png";
    private static final int COST = 0;

    public DoggoXD() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SummonDoggo((AbstractCreature)p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoggoXD();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            this.exhaust = false;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
