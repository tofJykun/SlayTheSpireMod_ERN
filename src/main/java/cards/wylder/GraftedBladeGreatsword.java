package cards.wylder;

import actions.GraftedBladeGreatswordAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PlatingPower;

public class GraftedBladeGreatsword extends CustomCard {
    public static final String ID = "GraftedBladeGreatsword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public GraftedBladeGreatsword() {
        this(true);
    }

    private GraftedBladeGreatsword(boolean preview) {
        super(ID, STRINGS.NAME, "img/cards/wylder/GraftedBladeGreatsword.png", 2, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 15;
        baseMagicNumber = magicNumber = 2;
        selfRetain = true;
        exhaust = true;
        if (preview) {
            cardsToPreview = new GraftedBladeGreatsword(false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GraftedBladeGreatswordAction(p, m,
                new DamageInfo(p, damage, damageTypeForTurn), upgraded));
        addToBot(new ApplyPowerAction(p, p, new PlatingPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(5);
            upgradeMagicNumber(1);
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
            if (cardsToPreview != null) {
                cardsToPreview.upgrade();
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GraftedBladeGreatsword();
    }
}
