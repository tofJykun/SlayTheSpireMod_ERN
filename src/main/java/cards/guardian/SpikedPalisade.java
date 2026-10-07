package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PerfectGuardPower;

public class SpikedPalisade extends CustomCard {
    public static final String ID = "SpikedPalisade";

    public SpikedPalisade() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/guardian/SpikedPalisade.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 3;
        isMultiDamage = true;
        baseMagicNumber = magicNumber = 2;
    }

    public int getPerfectGuardAmount() { return upgraded ? 6 : 3; }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        int guard = getPerfectGuardAmount();
        addToBot(new ApplyPowerAction(p, p, new PerfectGuardPower(p, guard), guard));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new SpikedPalisade(); }
}
