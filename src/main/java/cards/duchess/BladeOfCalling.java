package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ScheduledField;
import powers.BladeOfCallingPower;

public class BladeOfCalling extends AbstractScheduledCard {
    public static final String ID = "BladeOfCalling";
    private static final String IMG_PATH = "img/cards/duchess/BladeOfCalling.png";
    private static final int COST = -2;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int BLOODBURN = 2;
    private static final int UPGRADE_PLUS_BLOODBURN = 1;
    private static final int DEFAULT_SCHEDULED = 3;

    public BladeOfCalling() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY,
                DEFAULT_SCHEDULED);
        this.baseDamage = DAMAGE;
        this.baseBloodburn = BLOODBURN;
        this.bloodburn = this.baseBloodburn;
        onScheduledCountChanged(DEFAULT_SCHEDULED, DEFAULT_SCHEDULED);
    }

    private int baseBloodburn;
    private int bloodburn;

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                new BladeOfCallingPower((AbstractCreature)m, (AbstractCreature)p, this.bloodburn),
                this.bloodburn, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (ScheduledField.isPendingAutoplay(this)) {
            return super.canUse(p, m);
        }
        this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = base;
        this.magicNumber = current;
        this.isMagicNumberModified = current != base;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BladeOfCalling();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBloodburn(UPGRADE_PLUS_BLOODBURN);
        }
    }

    private void upgradeBloodburn(int amount) {
        this.baseBloodburn += amount;
        this.bloodburn = this.baseBloodburn;
    }

    public int getBloodburn() {
        return this.bloodburn;
    }

    public int getBaseBloodburn() {
        return this.baseBloodburn;
    }
}
