package cards.scholar;

import basemod.abstracts.CustomCard;
import cards.tempcards.MottledPot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PotentateCookbookPower;

public class PotentateCookbook extends CustomCard {
    public static final String ID = "PotentateCookbook";
    private static final String IMG_PATH = "img/cards/scholar/PotentateCookbook.png";
    private static final int COST = 1;

    public PotentateCookbook() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.cardsToPreview = new MottledPot();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new PotentateCookbookPower((AbstractCreature)p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PotentateCookbook();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
