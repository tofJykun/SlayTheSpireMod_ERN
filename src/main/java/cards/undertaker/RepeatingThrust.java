package cards.undertaker;

import actions.PlayRandomAttackHandCardAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class RepeatingThrust extends CustomCard {
    public static final String ID = "RepeatingThrust";
    private static final String IMG_PATH = "img/cards/undertaker/RepeatingThrust.png";
    private static final int COST = 2;
    private static final int DAMAGE = 6;
    private static final int ATTACKS = 1;

    public RepeatingThrust() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON,
                CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = ATTACKS;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot((AbstractGameAction)new PlayRandomAttackHandCardAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new RepeatingThrust();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
