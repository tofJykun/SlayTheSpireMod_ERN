package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PlayerDebuffStats;
import patches.AbstractCardEnum;

public class FingerprintSpear extends CustomCard {
    public static final String ID = "FingerprintSpear";

    public FingerprintSpear() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/FingerprintSpear.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 14;
        this.baseBlock = 14;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    private int reducedBase(int value) {
        return (int)Math.max(0L, (long)value - (long)PlayerDebuffStats.getApplications() * this.magicNumber);
    }

    @Override
    public void applyPowers() {
        int originalDamage = this.baseDamage;
        int originalBlock = this.baseBlock;
        this.baseDamage = reducedBase(originalDamage);
        this.baseBlock = reducedBase(originalBlock);
        try {
            super.applyPowers();
        } finally {
            this.baseDamage = originalDamage;
            this.baseBlock = originalBlock;
        }
        this.isDamageModified = this.damage != this.baseDamage;
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalDamage = this.baseDamage;
        int originalBlock = this.baseBlock;
        this.baseDamage = reducedBase(originalDamage);
        this.baseBlock = reducedBase(originalBlock);
        try {
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalDamage;
            this.baseBlock = originalBlock;
        }
        this.isDamageModified = this.damage != this.baseDamage;
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(player, player, this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeBlock(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FingerprintSpear();
    }
}
