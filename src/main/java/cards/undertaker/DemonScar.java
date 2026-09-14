package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.FaithPower;
import powers.LoseFaithAtEndOfTurnPower;

public class DemonScar extends CustomCard {
    public static final String ID = "DemonScar";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/undertaker/DemonScar.png";
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int TEMPORARY_FAITH = 3;
    private static final int UPGRADE_PLUS_FAITH = 2;

    public DemonScar() {
        this(true);
    }

    private DemonScar(boolean addPreview) {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = TEMPORARY_FAITH;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        if (addPreview) {
            this.cardsToPreview = new DemonScar(false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new FaithPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new LoseFaithAtEndOfTurnPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(makeStatEquivalentCopy(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DemonScar();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_FAITH);
            if (this.cardsToPreview != null && !this.cardsToPreview.upgraded) {
                this.cardsToPreview.upgrade();
            }
        }
    }
}
