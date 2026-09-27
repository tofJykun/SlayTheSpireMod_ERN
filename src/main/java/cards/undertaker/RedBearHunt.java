package cards.undertaker;

import actions.PlaceHandCardOnTopAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class RedBearHunt extends CustomCard {
    public static final String ID = "RedBearHunt";
    private static final String IMG_PATH = "img/cards/undertaker/RedBearHunt.png";
    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public RedBearHunt() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
        addToBot((AbstractGameAction)new PlaceHandCardOnTopAction(getCardStrings().EXTENDED_DESCRIPTION[0]));
        addToBot((AbstractGameAction)new DiscardAction((AbstractCreature)p, (AbstractCreature)p, 1, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RedBearHunt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }
}
