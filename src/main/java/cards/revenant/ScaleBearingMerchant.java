package cards.revenant;

import actions.ScaleBearingMerchantAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class ScaleBearingMerchant extends CustomCard {
    public static final String ID = "ScaleBearingMerchant";
    private static final String IMG_PATH = "img/cards/revenant/ScaleBearingMerchant.png";
    private static final int COST = 3;

    public ScaleBearingMerchant() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        refreshPreviews();
    }

    private static CardStrings getCardStrings() { return CardCrawlGame.languagePack.getCardStrings(ID); }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ScaleBearingMerchantAction(this.upgraded));
    }

    @Override public AbstractCard makeCopy() { return new ScaleBearingMerchant(); }

    private void refreshPreviews() {
        AbstractCard[] choices = {new PowerfulWeapon(), new ResistanceToAilments(), new LotOfRunes()};
        if (this.upgraded) {
            for (AbstractCard choice : choices) {
                choice.upgrade();
            }
        }
        MultiCardPreview.clear(this);
        MultiCardPreview.add(this, true, choices);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            refreshPreviews();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
