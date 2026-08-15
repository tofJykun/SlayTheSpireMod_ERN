package cards.undertaker;

import actions.SoulAppeaseAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SoulAppease extends CustomCard {
    public static final String ID = "SoulAppease";
    private static final String IMG_PATH = "img/cards/undertaker/SoulAppease.png";
    private static final int COST = 1;
    private static final int ATTACK_DAMAGE = 20;
    private static final int HEAL_AMOUNT = 10;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public SoulAppease() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DAMAGE;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new SoulAppeaseAction((AbstractCreature)p, this.multiDamage,
                this.damageTypeForTurn, HEAL_AMOUNT));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        resetDrawCount();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        resetDrawCount();
    }

    private void resetDrawCount() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SoulAppease();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }
}
