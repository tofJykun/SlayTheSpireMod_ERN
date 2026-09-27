package cards.recluse;

import actions.BedOfMagicToHandAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class MagicKinship extends CustomCard {
    public static final String ID = "MagicKinship";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/recluse/MagicKinship.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public MagicKinship() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.cardsToPreview = new BedOfMagic();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new BedOfMagicToHandAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicKinship();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
