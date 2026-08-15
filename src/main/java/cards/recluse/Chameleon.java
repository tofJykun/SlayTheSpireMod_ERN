package cards.recluse;

import actions.ChameleonAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Chameleon extends CustomCard {
    public static final String ID = "Chameleon";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/recluse/Chameleon.png";
    private static final int COST = 1;
    private static final int CARDS_TO_COPY = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;

    public Chameleon() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = CARDS_TO_COPY;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ChameleonAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Chameleon();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARDS);
        }
    }
}
