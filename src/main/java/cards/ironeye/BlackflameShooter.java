package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.GoldPaymentHelper;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

public class BlackflameShooter extends CustomCard {
    public static final String ID = "BlackflameShooter";
    public static final int HIT_COUNT = 20;
    public static final int BLOODBURN = 1;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public BlackflameShooter() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/BlackflameShooter.png", 3,
                STRINGS.DESCRIPTION, CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR,
                CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 1;
        baseMagicNumber = magicNumber = 40;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (GoldPaymentHelper.available(p) <= magicNumber) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseGold(magicNumber);
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot(new ApplyPowerAction(m, p, new BloodburnPower(m, p, BLOODBURN), BLOODBURN));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new BlackflameShooter(); }
}
