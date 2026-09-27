package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class PowerfulWeapon extends CustomCard {
    public static final String ID = "PowerfulWeapon";
    private static final String IMG_PATH = "img/cards/revenant/PowerfulWeapon.png";
    private static final int LOSS = 2;
    public PowerfulWeapon() {
        super(ID, strings().NAME, IMG_PATH, -2, strings().DESCRIPTION, CardType.SKILL,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.exhaust = true; this.baseMagicNumber = LOSS; this.magicNumber = LOSS;
        this.cardsToPreview = new WatcherStick();
    }
    private static CardStrings strings() { return CardCrawlGame.languagePack.getCardStrings(ID); }
    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        WatcherStick stick = new WatcherStick();
        if (this.upgraded) {
            stick.upgrade();
        }
        addToBot(new MakeTempCardInHandAction(stick, 1));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -this.magicNumber), -this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -this.magicNumber), -this.magicNumber));
    }
    @Override public AbstractCard makeCopy() { return new PowerfulWeapon(); }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(-1);
            this.cardsToPreview = new WatcherStick();
            this.cardsToPreview.upgrade();
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
