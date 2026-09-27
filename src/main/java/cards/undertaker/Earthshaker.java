package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.EarthshakerPower;

public class Earthshaker extends CustomCard {
    public static final String ID = "Earthshaker";
    private static final String IMG_PATH = "img/cards/undertaker/Earthshaker.png";
    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int DELAYED_TURNS = 2;

    public Earthshaker() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON,
                CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.baseMagicNumber = DELAYED_TURNS;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int[] delayedDamage = this.multiDamage.clone();
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new EarthshakerPower((AbstractCreature)p, DELAYED_TURNS, delayedDamage), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Earthshaker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
