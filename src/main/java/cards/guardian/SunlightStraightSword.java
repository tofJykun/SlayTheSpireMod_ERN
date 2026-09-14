package cards.guardian;

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
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.AbstractCardEnum;

public class SunlightStraightSword extends CustomCard {
    public static final String ID = "SunlightStraightSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/guardian/SunlightStraightSword.png";
    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    private int recordedDamageThisCombat = 0;
    private int displayDamageThisUse = 0;

    public SunlightStraightSword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void applyPowers() {
        applyPowersToBlock();
        int displayDamage = calculatePlayerSideDamage();
        setDisplayDamage(displayDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowersToBlock();
        int displayDamage = calculatePlayerSideDamage();
        this.displayDamageThisUse = displayDamage;
        float tmp = displayDamage;

        if (mo != null) {
            for (AbstractPower power : mo.powers) {
                tmp = power.atDamageReceive(tmp, this.damageTypeForTurn, this);
            }
            for (AbstractPower power : mo.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
            }
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        this.damage = MathUtils.floor(tmp);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        recordDamage(this.displayDamageThisUse > 0 ? this.displayDamageThisUse : calculatePlayerSideDamage());
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private int calculatePlayerSideDamage() {
        AbstractPlayer player = AbstractDungeon.player;
        float tmp = this.baseDamage;
        this.isDamageModified = false;

        if (player != null) {
            for (AbstractRelic relic : player.relics) {
                tmp = relic.atDamageModify(tmp, this);
            }
            for (AbstractPower power : player.powers) {
                tmp = power.atDamageGive(tmp, this.damageTypeForTurn, this);
            }
            tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            for (AbstractPower power : player.powers) {
                tmp = power.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
            }
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        return Math.max(this.recordedDamageThisCombat, MathUtils.floor(tmp));
    }

    private void setDisplayDamage(int displayDamage) {
        this.displayDamageThisUse = displayDamage;
        this.damage = displayDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    private void recordDamage(int damageToRecord) {
        if (damageToRecord <= this.recordedDamageThisCombat) {
            return;
        }
        for (AbstractCard card : GetAllInBattleInstances.get(this.uuid)) {
            if (card instanceof SunlightStraightSword) {
                ((SunlightStraightSword)card).recordedDamageThisCombat = damageToRecord;
                ((SunlightStraightSword)card).setDisplayDamage(damageToRecord);
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        SunlightStraightSword copy = (SunlightStraightSword)super.makeStatEquivalentCopy();
        copy.recordedDamageThisCombat = this.recordedDamageThisCombat;
        copy.displayDamageThisUse = this.displayDamageThisUse;
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SunlightStraightSword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
