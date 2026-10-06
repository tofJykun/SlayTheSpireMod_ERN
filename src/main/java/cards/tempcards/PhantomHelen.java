package cards.tempcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.AbstractSummonPower;
import powers.SpiritPower;
import powers.SummonHelen;
import summons.SummonAnimationManager;
import relics.WhetstoneKnife;

public class PhantomHelen extends AbstractPhantomCard {
    public static final String ID = "PhantomHelen";
    private static final String IMG_PATH = "img/cards/tempcards/PhantomHelen.png";
    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    public PhantomHelen() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseMagicNumber = DAMAGE;
        this.magicNumber = this.baseMagicNumber;
        WhetstoneKnife.refreshPhantomDamage(this);
        this.exhaust = true;
        this.isEthereal = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (!AbstractSummonPower.isActiveSummon((AbstractCreature)p, SummonHelen.POWER_ID)) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null
                && AbstractSummonPower.isActiveSummon((AbstractCreature)AbstractDungeon.player, SummonHelen.POWER_ID)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SpiritPower.applyPhantomNumber(this);
        WhetstoneKnife.refreshPhantomDamage(this);
        SummonAnimationManager.triggerAttack(SummonHelen.SUMMON_KEY);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        SpiritPower.applyPhantomNumber(this);
        WhetstoneKnife.refreshPhantomDamage(this);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        SpiritPower.applyPhantomNumber(this);
        WhetstoneKnife.refreshPhantomDamage(this);
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new PhantomHelen();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE);
            this.upgradedDamage = true;
            WhetstoneKnife.refreshPhantomDamage(this);
        }
    }
}
