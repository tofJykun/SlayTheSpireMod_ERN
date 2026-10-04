package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.StunPower;

public class LifestealFist extends CustomCard {
    public static final String ID = "LifestealFist";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public LifestealFist() {
        super(ID, STRINGS.NAME, "img/cards/wylder/LifestealFist.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 10;
        baseMagicNumber = magicNumber = 10;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        super.calculateCardDamage(monster);
        if (monster != null && (monster.hasPower(StunPower.POWER_ID)
                || monster.intent == AbstractMonster.Intent.STUN)) {
            damage *= magicNumber;
            isDamageModified = damage != baseDamage;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LifestealFist();
    }
}
