package cards.undertaker;

import actions.BlinkboltAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Blinkbolt extends CustomCard {
    public static final String ID = "Blinkbolt";
    private static final String IMG_PATH = "img/cards/undertaker/Blinkbolt.png";

    public Blinkbolt() {
        super(ID, strings().NAME, IMG_PATH, 1, strings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 3;
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getDrawPerEnemy() {
        return 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BlinkboltAction(this, this.magicNumber, getDrawPerEnemy()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blinkbolt();
    }
}
