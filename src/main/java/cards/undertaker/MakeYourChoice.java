package cards.undertaker;

import actions.MakeYourChoiceAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class MakeYourChoice extends CustomCard {
    public static final String ID = "MakeYourChoice";
    private static final String IMG_PATH = "img/cards/undertaker/MakeYourChoice.png";

    public MakeYourChoice() {
        super(ID, strings().NAME, IMG_PATH, 2, strings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
        refreshPreviews();
    }

    private static CardStrings strings() { return CardCrawlGame.languagePack.getCardStrings(ID); }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new MakeYourChoiceAction(this.upgraded));
    }

    @Override public AbstractCard makeCopy() { return new MakeYourChoice(); }

    private void refreshPreviews() {
        AbstractCard[] choices = {new GreatStrength(), new GreatDexterity(), new DeepInsight()};
        if (this.upgraded) {
            for (AbstractCard choice : choices) {
                choice.upgrade();
            }
        }
        MultiCardPreview.clear(this);
        MultiCardPreview.add(this, true, choices);
    }

    @Override public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            refreshPreviews();
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
