package cards.duchess;

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
import patches.ReplayField;

public class Twindaggers extends CustomCard {
    public static final String ID = "Twindaggers";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Twindaggers() {
        super(ID, STRINGS.NAME, "img/cards/duchess/Twindaggers.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 1;
        ReplayField.setReplay(this, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Twindaggers();
    }
}
