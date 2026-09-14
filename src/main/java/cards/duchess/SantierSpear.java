package cards.duchess;

import actions.SantierSpearCounterAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SantierSpear extends CustomCard {
    public static final String ID = "SantierSpear";
    private static final String IMG_PATH = "img/cards/duchess/SantierSpear.png";
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int BASE_THRESHOLD = 5;
    private static final int UPGRADED_THRESHOLD = 3;
    private static final int BASE_DRAW = 1;
    private static final int REINFORCED_DRAW = 2;
    private static final int BASE_HITS = 1;
    private static final int REINFORCED_HITS = 2;
    private int playsThisCombat = 0;
    private boolean reinforced = false;

    public SantierSpear() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = BASE_DRAW;
        this.magicNumber = this.baseMagicNumber;
        updateForm();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = this.reinforced ? REINFORCED_HITS : BASE_HITS;
        for (int i = 0; i < hits; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        addToBot((AbstractGameAction)new DrawCardAction(this.magicNumber));
        addToBot((AbstractGameAction)new SantierSpearCounterAction(this.uuid));
    }

    public void incrementPlaysThisCombat() {
        this.playsThisCombat++;
        updateForm();
    }

    private int getThreshold() {
        return this.upgraded ? UPGRADED_THRESHOLD : BASE_THRESHOLD;
    }

    private int getRemainingPlays() {
        return Math.max(0, getThreshold() - this.playsThisCombat);
    }

    private void updateForm() {
        boolean shouldBeReinforced = this.playsThisCombat >= getThreshold();
        if (shouldBeReinforced && !this.reinforced) {
            this.reinforced = true;
            this.cost = 0;
            this.costForTurn = 0;
            this.isCostModified = true;
            this.isCostModifiedForTurn = false;
        } else if (!shouldBeReinforced) {
            this.reinforced = false;
        }
        this.baseMagicNumber = this.reinforced ? REINFORCED_DRAW : BASE_DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.rawDescription = this.reinforced
                ? getCardStrings().EXTENDED_DESCRIPTION[1]
                : getCardStrings().DESCRIPTION + getRemainingPlays() + getCardStrings().EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateForm();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        updateForm();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        SantierSpear copy = (SantierSpear)super.makeStatEquivalentCopy();
        copy.playsThisCombat = this.playsThisCombat;
        copy.reinforced = this.reinforced;
        copy.updateForm();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SantierSpear();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            updateForm();
        }
    }
}
