package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.RandomUsageCounter;
import patches.AbstractCardEnum;

public class Ergodic extends CustomCard {
    public static final String ID = "Ergodic";
    private static final String IMG_PATH = "img/cards/scholar/Ergodic.png";
    private static final int COST = 1;
    private static final int MULTIPLIER = 2;
    private static final int UPGRADE_PLUS_MULTIPLIER = 1;

    public Ergodic() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = MULTIPLIER;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void applyPowers() {
        setDamageFromRandomUsage();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromRandomUsage();
        super.calculateCardDamage(mo);
    }

    private void setDamageFromRandomUsage() {
        this.baseDamage = RandomUsageCounter.getCombatRandomUses() * this.magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ergodic();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER);
        }
    }
}
