package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class ImpenetrableThorns extends CustomCard {
    public static final String ID = "ImpenetrableThorns";
    private static final String IMG_PATH = "img/cards/undertaker/ImpenetrableThorns.png";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public ImpenetrableThorns() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, -2, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // This card cannot be played; it only has an effect when exhausted.
    }

    @Override
    public AbstractCard makeCopy() {
        return new ImpenetrableThorns();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
