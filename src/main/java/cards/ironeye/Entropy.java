package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.TransformStats;
import patches.AbstractCardEnum;

public class Entropy extends CustomCard {
    public static final String ID = "Entropy";
    private static final String IMG_PATH = "img/cards/ironeye/Entropy.png";
    private static final int COST = 1;
    private static final int MULTIPLIER = 3;
    private static final int UPGRADE_PLUS_MULTIPLIER = 1;

    public Entropy() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = MULTIPLIER;
        this.magicNumber = this.baseMagicNumber;
        this.baseDamage = 0;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void applyPowers() {
        setDamageFromTransforms();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromTransforms();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = 0;
    }

    private void setDamageFromTransforms() {
        this.baseDamage = TransformStats.getCombatTransforms() * this.magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Entropy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER);
        }
    }
}
