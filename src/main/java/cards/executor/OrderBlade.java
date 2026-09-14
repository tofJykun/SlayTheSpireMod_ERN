package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;

import java.util.HashSet;
import java.util.Set;

public class OrderBlade extends CustomCard {
    public static final String ID = "OrderBlade";
    private static final String IMG_PATH = "img/cards/executor/OrderBlade.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 7;
    private static final int UPGRADE_PLUS_DMG = 2;

    public OrderBlade() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }

        int damageMultiplier = 1 + countDebuffTypes(m);
        DamageInfo info = new DamageInfo((AbstractCreature)p,
                calculateTotalDamage(m, damageMultiplier), this.damageTypeForTurn);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m, info,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private int calculateTotalDamage(AbstractMonster target, int damageMultiplier) {
        int originalBaseDamage = this.baseDamage;
        int originalDamage = this.damage;
        boolean originalIsDamageModified = this.isDamageModified;

        this.baseDamage = originalBaseDamage * damageMultiplier;
        super.calculateCardDamage(target);
        int totalDamage = this.damage;

        this.baseDamage = originalBaseDamage;
        this.damage = originalDamage;
        this.isDamageModified = originalIsDamageModified;
        return totalDamage;
    }

    private static int countDebuffTypes(AbstractCreature target) {
        Set<String> debuffs = new HashSet<>();
        for (AbstractPower power : target.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF && !"Shackled".equals(power.ID)) {
                debuffs.add(power.ID);
            }
        }
        return debuffs.size();
    }

    @Override
    public AbstractCard makeCopy() {
        return new OrderBlade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
