package cards.wylder;

import actions.WolfGreatswordValueLossAction;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.AbstractCardEnum;

public class WolfGreatsword extends CustomCard {
    public static final String ID = "WolfGreatsword";
    private static final String IMG_PATH = "img/cards/wylder/WolfGreatsword.png";
    private static final int COST = 2;
    private static final int DAMAGE = 25;
    private static final int VALUE_LOSS = 15;
    private static final int UPGRADE_PLUS_VALUE_LOSS = 10;

    private int signedDamage;

    public WolfGreatsword() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = VALUE_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.signedDamage = this.baseDamage;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, Math.abs(this.signedDamage), this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot((AbstractGameAction)new WolfGreatswordValueLossAction(this.uuid, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        applySignedDamage(null);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applySignedDamage(mo);
    }

    private void applySignedDamage(AbstractMonster target) {
        this.signedDamage = calculateSignedDamage(target);
        this.damage = Math.abs(this.signedDamage);
        this.isDamageModified = this.signedDamage != getSignedBaseDamage();
    }

    private int calculateSignedDamage(AbstractMonster target) {
        AbstractPlayer player = AbstractDungeon.player;
        float tmp = getSignedBaseDamage();
        if (player == null) {
            return MathUtils.floor(tmp);
        }

        for (AbstractRelic relic : player.relics) {
            tmp = relic.atDamageModify(tmp, this);
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageGive(tmp, this.damageTypeForTurn, this);
        }
        tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageTypeForTurn);
            }
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
        }
        if (target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageTypeForTurn);
            }
        }
        return MathUtils.floor(tmp);
    }

    public int getSignedDamage() {
        return this.signedDamage;
    }

    public int getSignedBaseDamage() {
        return this.baseDamage - this.misc;
    }

    public void increaseValueLoss(int lossAmount) {
        this.misc += lossAmount;
        this.signedDamage = getSignedBaseDamage();
        this.damage = Math.abs(this.signedDamage);
        initializeDescription();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.signedDamage = getSignedBaseDamage();
        this.damage = Math.abs(this.signedDamage);
        this.isDamageModified = false;
    }

    @Override
    public void onMoveToDiscard() {
        this.signedDamage = getSignedBaseDamage();
        this.damage = Math.abs(this.signedDamage);
        this.isDamageModified = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new WolfGreatsword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VALUE_LOSS);
        }
    }
}
