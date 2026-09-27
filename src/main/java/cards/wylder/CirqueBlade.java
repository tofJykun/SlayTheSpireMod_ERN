package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class CirqueBlade extends CustomCard {
    public static final String ID = "CirqueBlade";
    private static final String IMG_PATH = "img/cards/wylder/CirqueBlade.png";
    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int SELF_DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    public CirqueBlade() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = SELF_DAMAGE;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage,
                this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DamageAction((AbstractCreature)p,
                new DamageInfo((AbstractCreature)p, this.magicNumber, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CirqueBlade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
