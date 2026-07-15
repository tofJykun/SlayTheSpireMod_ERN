package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import powers.BloodburnPower;
import powers.ScarletRotPower;

public class CodeX extends CustomCard {
    public static final String ID = "CodeX";
    private static final String IMG_PATH = "img/cards/tempcards/CodeX.png";
    private static final int DEFAULT_COST = 0;
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_BLOCK = 1;
    private static final int DEFAULT_ANOMALY = 1;
    private static final int MAX_COST = 3;
    private static final int MAX_DAMAGE_OR_BLOCK = 10;
    private static final int MAX_ANOMALY = 3;

    public int basePoison;
    public int poison;
    public int baseScarletRot;
    public int scarletRot;
    public int baseBloodburn;
    public int bloodburn;

    public CodeX() {
        super(ID, getCardStrings().NAME, IMG_PATH, DEFAULT_COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = DEFAULT_DAMAGE;
        this.baseBlock = DEFAULT_BLOCK;
        this.basePoison = DEFAULT_ANOMALY;
        this.poison = this.basePoison;
        this.baseScarletRot = DEFAULT_ANOMALY;
        this.scarletRot = this.baseScarletRot;
        this.baseBloodburn = DEFAULT_ANOMALY;
        this.bloodburn = this.baseBloodburn;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public static CodeX createRandom() {
        CodeX card = new CodeX();
        card.randomizeValues();
        return card;
    }

    private void randomizeValues() {
        setPermanentCost(random(0, MAX_COST));
        this.baseDamage = random(1, MAX_DAMAGE_OR_BLOCK);
        this.damage = this.baseDamage;
        this.baseBlock = random(1, MAX_DAMAGE_OR_BLOCK);
        this.block = this.baseBlock;
        this.basePoison = random(1, MAX_ANOMALY);
        this.poison = this.basePoison;
        this.baseScarletRot = random(1, MAX_ANOMALY);
        this.scarletRot = this.baseScarletRot;
        this.baseBloodburn = random(1, MAX_ANOMALY);
        this.bloodburn = this.baseBloodburn;
        initializeDescription();
    }

    private static int random(int min, int max) {
        if (AbstractDungeon.cardRandomRng == null) {
            return min;
        }
        return AbstractDungeon.cardRandomRng.random(min, max);
    }

    private void setPermanentCost(int newCost) {
        this.cost = newCost;
        this.costForTurn = newCost;
        this.isCostModified = false;
        this.isCostModifiedForTurn = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PoisonPower((AbstractCreature)m, (AbstractCreature)p, this.poison),
                    this.poison, AbstractGameAction.AttackEffect.POISON));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new ScarletRotPower((AbstractCreature)m, (AbstractCreature)p, this.scarletRot),
                    this.scarletRot, AbstractGameAction.AttackEffect.POISON));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new BloodburnPower((AbstractCreature)m, (AbstractCreature)p, this.bloodburn),
                    this.bloodburn, AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        resetCustomVariables();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        resetCustomVariables();
    }

    private void resetCustomVariables() {
        this.poison = this.basePoison;
        this.scarletRot = this.baseScarletRot;
        this.bloodburn = this.baseBloodburn;
    }

    @Override
    public AbstractCard makeCopy() {
        return new CodeX();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        CodeX card = (CodeX)super.makeStatEquivalentCopy();
        card.basePoison = this.basePoison;
        card.poison = this.poison;
        card.baseScarletRot = this.baseScarletRot;
        card.scarletRot = this.scarletRot;
        card.baseBloodburn = this.baseBloodburn;
        card.bloodburn = this.bloodburn;
        return card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
