package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.BloodlossPower;
import powers.FrostbitePower;
import powers.MadnessPower;
import powers.SleepPower;

public class Shackle extends CustomCard {
    public static final String ID = "Shackle";
    private static final String IMG_PATH = "img/cards/executor/Shackle.png";

    public Shackle() {
        super(ID, getCardStrings().NAME, IMG_PATH, 0, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 0;
        this.isMultiDamage = true;
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster monster, float damage) {
        if (monster == null) {
            return damage;
        }
        // BaseMod calls this for each target before Strength, Weak and Vulnerable.
        for (AbstractPower power : monster.powers) {
            if (FrostbitePower.POWER_ID.equals(power.ID) || SleepPower.POWER_ID.equals(power.ID)
                    || MadnessPower.POWER_ID.equals(power.ID) || BloodlossPower.POWER_ID.equals(power.ID)) {
                damage += Math.max(0, power.amount) * (float)this.magicNumber;
            }
        }
        return damage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        // Keep zero-damage targets in the normal attack pipeline (including Thorns).
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Shackle();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
