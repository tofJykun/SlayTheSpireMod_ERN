package cards.duchess;

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

public class ConvenientBundlingStrap extends CustomCard implements PackagingCard {
    public static final String ID = "ConvenientBundlingStrap";
    private static final String IMG_PATH = "img/cards/duchess/ConvenientBundlingStrap.png";
    private static final int COST = 1;
    private static final int PACKAGE = 3;
    private static final int UPGRADE_PLUS_PACKAGE = 1;

    private final ArrayList<AbstractCard> packagedCards = new ArrayList<AbstractCard>();
    private boolean needsPostUseRefresh;
    private boolean needsClearAfterUseAction;

    public ConvenientBundlingStrap() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = PACKAGE;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.packagedCards.isEmpty()) {
            addToBot(new TakeoutBoxPackageAction(this, this.magicNumber, getCardStrings().EXTENDED_DESCRIPTION[0]));
        } else {
            this.needsClearAfterUseAction = true;
            addToBot(new TakeoutBoxUnpackAction(this, this, false));
        }
    }

    @Override
    public ArrayList<AbstractCard> getPackagedCards() {
        return this.packagedCards;
    }

    @Override
    public void setPackagedCards(ArrayList<AbstractCard> cards) {
        this.packagedCards.clear();
        this.packagedCards.addAll(cards);
        this.needsPostUseRefresh = true;
    }

    @Override
    public void clearPackagedCards() {
        this.packagedCards.clear();
        this.needsPostUseRefresh = false;
        this.needsClearAfterUseAction = false;
        updateDynamicDescription();
    }

    @Override
    public void refreshAfterPackagingUseAction() {
        if (this.needsPostUseRefresh) {
            this.needsPostUseRefresh = false;
            updateDynamicDescription();
        } else if (this.needsClearAfterUseAction) {
            clearPackagedCards();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ConvenientBundlingStrap();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        ConvenientBundlingStrap copy = (ConvenientBundlingStrap)super.makeStatEquivalentCopy();
        copy.packagedCards.clear();
        for (AbstractCard card : this.packagedCards) {
            copy.packagedCards.add(card.makeStatEquivalentCopy());
        }
        copy.needsPostUseRefresh = false;
        copy.needsClearAfterUseAction = false;
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
            this.exhaust = false;
            setCardPreviews();
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.packagedCards.size(); i++) {
                if (i > 0) {
                    builder.append(strings.EXTENDED_DESCRIPTION[1]);
                }
                builder.append("*").append(this.packagedCards.get(i).name.replace(" ", " *"));
            }
            builder.append(strings.EXTENDED_DESCRIPTION[2]);
            this.rawDescription = builder.toString();
            this.exhaust = true;
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
