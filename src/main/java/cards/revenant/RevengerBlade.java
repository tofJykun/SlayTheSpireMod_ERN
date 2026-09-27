package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;
import powers.SummonFrederick;
import powers.SummonHelen;
import powers.SummonSebastian;

public class RevengerBlade extends CustomCard {
    public static final String ID = "RevengerBlade";

    public RevengerBlade() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/revenant/RevengerBlade.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 9;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        applySummonMultiplier();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        applySummonMultiplier();
    }

    private void applySummonMultiplier() {
        if (AbstractSummonPower.hasSummonPower(AbstractDungeon.player)) {
            this.damage *= this.magicNumber;
            this.isDamageModified = this.damage != this.baseDamage;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasSummon = AbstractSummonPower.hasSummonPower(p);
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (!hasSummon) {
            addToBot(new ApplyPowerAction(p, p, getRandomBasicSummon(p), 1));
        }
    }

    private AbstractPower getRandomBasicSummon(AbstractPlayer p) {
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                return new SummonHelen(p);
            case 1:
                return new SummonFrederick(p);
            default:
                return new SummonSebastian(p);
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RevengerBlade();
    }
}
