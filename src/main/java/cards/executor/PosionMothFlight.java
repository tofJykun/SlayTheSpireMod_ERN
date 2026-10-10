package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import patches.AbstractCardEnum;

public class PosionMothFlight extends CustomCard {
    public static final String ID = "PosionMothFlight";

    public PosionMothFlight() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/PosionMothFlight.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 6;
    }

    private int poisonAmount(AbstractMonster monster) {
        AbstractPower poison = monster == null ? null : monster.getPower(PoisonPower.POWER_ID);
        return poison == null ? 0 : Math.max(0, poison.amount);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.damage = 0;
        this.isDamageModified = false;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int poison = poisonAmount(monster);
        int originalBaseDamage = this.baseDamage;
        this.baseDamage = (int)Math.min(Integer.MAX_VALUE,
                (long)originalBaseDamage + (long)poison * this.magicNumber);
        try {
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalBaseDamage;
        }
        if (poison == 0) {
            this.damage = 0;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (poisonAmount(monster) <= 0) {
            return;
        }
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new RemoveSpecificPowerAction(monster, player, PoisonPower.POWER_ID));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PosionMothFlight();
    }
}
