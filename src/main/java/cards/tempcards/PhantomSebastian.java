package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.SummonSebastian;

public class PhantomSebastian extends CustomCard {
    public static final String ID = "PhantomSebastian";
    private static final String IMG_PATH = "img/cards/tempcards/FadingPrimalGlintstone.png";
    private static final int COST = 2;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int HIT_COUNT = 3;

    public PhantomSebastian() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = DAMAGE;
        this.magicNumber = this.baseMagicNumber;
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
        if (!p.hasPower(SummonSebastian.POWER_ID)) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new DamageRandomEnemyAction(
                    new DamageInfo((AbstractCreature)p, this.magicNumber, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new PhantomSebastian();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE);
        }
    }
}
