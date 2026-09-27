package cards.undertaker;

import actions.WeedCutterAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class WeedCutter extends CustomCard {
    public static final String ID = "WeedCutter";
    private static final String IMG_PATH = "img/cards/undertaker/WeedCutter.png";

    public WeedCutter() {
        super(ID, getCardStrings().NAME, IMG_PATH, 2, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new WeedCutterAction(p));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WeedCutter();
    }
}
