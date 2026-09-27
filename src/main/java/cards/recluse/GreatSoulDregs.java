package cards.recluse;

import basemod.abstracts.CustomCard;
import cards.status.MagicEmber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import general.CombatState;

public class GreatSoulDregs extends CustomCard {
    public static final String ID = "GreatSoulDregs";
    private static final String IMG_PATH = "img/cards/recluse/GreatSoulDregs.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 18;
    private static final int DAMAGE_PER_HP_LOSS = 3;
    private static final int UPGRADE_PLUS_MULTIPLIER = 2;
    public static final int MAGIC_EMBERS = 3;

    public GreatSoulDregs() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = this.magicNumber = DAMAGE_PER_HP_LOSS;
        this.cardsToPreview = new MagicEmber();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(new MagicEmber(), MAGIC_EMBERS));
    }

    private int hpLossBonus() {
        if (!CombatState.isInCombat() || AbstractDungeon.player == null) {
            return 0;
        }
        return AbstractDungeon.player.damagedThisCombat * this.magicNumber;
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += hpLossBonus();
        try {
            super.applyPowers();
        } finally {
            this.baseDamage = originalBaseDamage;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += hpLossBonus();
        try {
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalBaseDamage;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new GreatSoulDregs();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER);
        }
    }
}
