package cards.recluse;

import actions.RandomDamageWithPowersAction;
import basemod.abstracts.CustomCard;
import cards.status.MagicEmber;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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

public class DarkBead extends CustomCard {
    public static final String ID = "DarkBead";
    private static final String IMG_PATH = "img/cards/recluse/DarkBead.png";
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int MAGIC_DMG = 3;
    private static final int HIT_COUNT = 3;
    private static final int HAND_SIZE = 10;

    public DarkBead() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = MAGIC_DMG;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new MagicEmber();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new RandomDamageWithPowersAction((AbstractCreature)p, this,
                    ATTACK_DMG, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        int magicDamage = calculateMagicDamage(null, false);
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new RandomDamageWithPowersAction((AbstractCreature)p, this,
                    magicDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        }

        int cardsToCreate = HAND_SIZE - p.hand.size();
        if (cardsToCreate > 0) {
            addToBot((AbstractGameAction)new MakeTempCardInHandAction(new MagicEmber(), cardsToCreate));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        applyOrdinaryDamagePowers(null);
        applyMagicDamagePowers(null);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        applyOrdinaryDamagePowers(mo);
        applyMagicDamagePowers(mo);
    }

    private void applyMagicDamagePowers(AbstractMonster target) {
        this.magicNumber = calculateMagicDamage(target, target != null);
        this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
    }

    private int calculateMagicDamage(AbstractMonster target, boolean includeTargetPowers) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            return this.baseMagicNumber;
        }

        float tmp = this.baseMagicNumber;
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        for (AbstractRelic relic : player.relics) {
            tmp = relic.atDamageModify(tmp, this);
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageGive(tmp, this.damageTypeForTurn, this);
        }
        tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (includeTargetPowers && target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageTypeForTurn);
            }
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageFinalGive(tmp, this.damageTypeForTurn);
        }
        if (includeTargetPowers && target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageTypeForTurn);
            }
        }
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        return MathUtils.floor(tmp);
    }

    private void applyOrdinaryDamagePowers(AbstractMonster target) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            this.damage = this.baseDamage;
            this.isDamageModified = false;
            return;
        }

        float tmp = this.baseDamage;
        for (AbstractRelic relic : player.relics) {
            tmp = relic.atDamageModify(tmp, this);
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageGive(tmp, this.damageTypeForTurn);
        }
        tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageTypeForTurn);
            }
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageFinalGive(tmp, this.damageTypeForTurn);
        }
        if (target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageTypeForTurn);
            }
        }
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        this.damage = MathUtils.floor(tmp);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new DarkBead();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
