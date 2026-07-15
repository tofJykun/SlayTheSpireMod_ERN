package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class Bonewheel extends CustomCard {
    public static final String ID = "Bonewheel";
    private static final String IMG_PATH = "img/cards/guardian/Bonewheel.png";
    private static final int COST = -1;
    private static final int HIT_COUNT = 9;
    private static final int UPGRADE_PLUS_HIT_COUNT = 3;

    public Bonewheel() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = HIT_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = getXValue(p);
        calculateCardDamage(m);
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        this.baseDamage = getXValue(AbstractDungeon.player);
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = getXValue(AbstractDungeon.player);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = 0;
    }

    private int getXValue(AbstractPlayer p) {
        int value = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            value = this.energyOnUse;
        }
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bonewheel();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HIT_COUNT);
        }
    }
}
