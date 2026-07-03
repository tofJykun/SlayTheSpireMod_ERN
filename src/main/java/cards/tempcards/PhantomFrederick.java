package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.SummonFrederick;

public class PhantomFrederick extends CustomCard {
    public static final String ID = "PhantomFrederick";
    private static final String IMG_PATH = "img/cards/tempcards/FadingPrimalGlintstone.png";
    private static final int COST = 1;
    private static final int VALUE = 6;
    private static final int UPGRADE_PLUS_VALUE = 2;

    public PhantomFrederick() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseMagicNumber = VALUE;
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
        if (!p.hasPower(SummonFrederick.POWER_ID)) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.magicNumber, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new PhantomFrederick();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VALUE);
        }
    }
}
