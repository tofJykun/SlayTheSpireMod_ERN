package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class CrownOfThorns extends CustomCard {
    public static final String ID = "CrownOfThorns";
    public static final int HP_LOSS = 33;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrownOfThorns() {
        super(ID, STRINGS.NAME, "img/cards/scholar/CrownOfThorns.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.isInnate = true;
        this.exhaust = true;
        this.cardsToPreview = new Apotheosis();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = new Apotheosis();
        if (this.upgraded) {
            card.upgrade();
        }
        addToBot(new MakeTempCardInHandAction(card, 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.cardsToPreview.upgrade();
            this.rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrownOfThorns();
    }
}
