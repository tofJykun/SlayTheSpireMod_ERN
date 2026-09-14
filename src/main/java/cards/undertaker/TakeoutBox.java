package cards.undertaker;

import actions.TakeoutBoxPackageAction;
import actions.TakeoutBoxUnpackAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PackagingCard;
import patches.AbstractCardEnum;

import java.util.ArrayList;

public class TakeoutBox extends CustomCard implements PackagingCard {
    public static final String ID = "TakeoutBox";
    private static final String IMG_PATH = "img/cards/undertaker/TakeoutBox.png";
    private static final int COST = 2;
    private static final int PACKAGE = 2;
    private static final int UPGRADE_PLUS_PACKAGE = 1;

    private final ArrayList<AbstractCard> packagedCards = new ArrayList<AbstractCard>();

    public TakeoutBox() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = PACKAGE;
        this.magicNumber = this.baseMagicNumber;
        this.selfRetain = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.packagedCards.isEmpty()) {
            addToBot(new TakeoutBoxPackageAction(this, this.magicNumber, getCardStrings().EXTENDED_DESCRIPTION[0]));
        } else {
            addToBot(new TakeoutBoxUnpackAction(this, this));
        }
    }

    public ArrayList<AbstractCard> getPackagedCards() {
        return this.packagedCards;
    }

    public void setPackagedCards(ArrayList<AbstractCard> cards) {
        this.packagedCards.clear();
        this.packagedCards.addAll(cards);
        updateDynamicDescription();
    }

    public void clearPackagedCards() {
        this.packagedCards.clear();
        updateDynamicDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new TakeoutBox();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        TakeoutBox copy = (TakeoutBox)super.makeStatEquivalentCopy();
        copy.packagedCards.clear();
        for (AbstractCard card : this.packagedCards) {
            copy.packagedCards.add(card.makeStatEquivalentCopy());
        }
        copy.updateDynamicDescription();
        return copy;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PACKAGE);
            updateDynamicDescription();
        }
    }

    private void updateDynamicDescription() {
        CardStrings strings = getCardStrings();
        if (this.packagedCards.isEmpty()) {
            this.rawDescription = strings.DESCRIPTION;
            setCardPreviews();
        } else {
            StringBuilder builder = new StringBuilder(strings.EXTENDED_DESCRIPTION[1]);
            for (int i = 0; i < this.packagedCards.size(); i++) {
                if (i > 0) {
                    builder.append(strings.EXTENDED_DESCRIPTION[2]);
                }
                builder.append("*").append(this.packagedCards.get(i).name.replace(" ", " *"));
            }
            builder.append(strings.EXTENDED_DESCRIPTION[3]);
            this.rawDescription = builder.toString();
            setCardPreviews(this.packagedCards.toArray(new AbstractCard[0]));
        }
        initializeDescription();
    }

    private void setCardPreviews(AbstractCard... previews) {
        this.cardsToPreview = null;
        MultiCardPreview.clear(this);
        ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
        for (AbstractCard preview : previews) {
            if (preview != null) {
                cards.add(preview.makeStatEquivalentCopy());
            }
        }
        if (cards.size() == 1) {
            this.cardsToPreview = cards.get(0);
        } else if (cards.size() > 1) {
            MultiCardPreview.add(this, true, cards.toArray(new AbstractCard[0]));
        }
    }
}
