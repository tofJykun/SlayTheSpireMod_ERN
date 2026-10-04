package cards.duchess;

import actions.UrumiAction;
import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ScheduledField;

public class Urumi extends AbstractScheduledCard {
    public static final String ID = "Urumi";

    public Urumi() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/duchess/Urumi.png", 3,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON,
                CardTarget.ALL_ENEMY, 3);
        baseDamage = 9;
        baseMagicNumber = magicNumber = 1;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new UrumiAction(p, multiDamage, damageTypeForTurn, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            ScheduledField.setBaseScheduled(this, 2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Urumi();
    }
}
