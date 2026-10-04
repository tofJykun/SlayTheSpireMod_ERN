package cards.duchess;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import patches.AbstractCardEnum;

public class Misericorde extends CustomCard {
    public static final String ID = "Misericorde";

    public Misericorde() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/duchess/Misericorde.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 1;
        CardModifierManager.addModifier(this, new VulnerableScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Misericorde();
    }

    public static class VulnerableScaling extends AbstractCardModifier {
        @Override
        public boolean isInherent(AbstractCard card) {
            return true;
        }

        @Override
        public float modifyDamageFinal(float damage, DamageInfo.DamageType type,
                                       AbstractCard card, AbstractMonster target) {
            AbstractPower vulnerable = target == null ? null : target.getPower(VulnerablePower.POWER_ID);
            if (vulnerable == null || vulnerable.amount <= 0) return damage;
            // Multiply before final power hooks and rounding; Vulnerable still applies normally.
            return damage * (1.0f + vulnerable.amount * (float)card.magicNumber);
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new VulnerableScaling();
        }
    }
}
