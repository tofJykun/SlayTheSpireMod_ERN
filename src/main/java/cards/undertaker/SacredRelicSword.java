package cards.undertaker;

import actions.SacredRelicSwordDamageAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SacredRelicSword extends CustomCard {
    public static final String ID = "SacredRelicSword";
    public static final int GOLD_PER_HIT = 5;

    public SacredRelicSword() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/undertaker/SacredRelicSword.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 3;
        isMultiDamage = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new SacredRelicSwordDamageAction(p, multiDamage, damageTypeForTurn, GOLD_PER_HIT));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacredRelicSword();
    }
}
