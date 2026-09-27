package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;
import powers.FaithPower;
import powers.LoseFaithAtEndOfTurnPower;

public class Grant extends CustomCard {
    public static final String ID = "Grant";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/undertaker/Grant.png";
    private static final int COST = 2;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DAMAGE = 5;
    private static final int WEAK = 2;
    private static final int TEMPORARY_FAITH = 2;
    private static final int UPGRADE_PLUS_FAITH = 1;

    public Grant() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = TEMPORARY_FAITH;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new WeakPower((AbstractCreature)m, WEAK, false), WEAK));
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new FaithPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new LoseFaithAtEndOfTurnPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Grant();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_FAITH);
        }
    }
}
