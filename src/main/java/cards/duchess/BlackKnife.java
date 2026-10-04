package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BloodburnPower;
import powers.HornetRingPower;

public class BlackKnife extends CustomCard {
    public static final String ID = "BlackKnife";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public BlackKnife() {
        super(ID, STRINGS.NAME, "img/cards/duchess/BlackKnife.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 12;
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public int getBloodburn() {
        return this.upgraded ? 12 : 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;
        boolean attacking = HornetRingPower.isAttackIntent(m.intent);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (attacking) {
            addToBot(new DrawCardAction(p, this.magicNumber));
        } else {
            addToBot(new ApplyPowerAction(m, p, new BloodburnPower(m, p, getBloodburn()), getBloodburn()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackKnife();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
