package cards.executor;

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
import powers.PoiseBreakPower;

public class GiantHunt extends CustomCard {
    public static final String ID = "GiantHunt";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/executor/GiantHunt.png";
    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DAMAGE = 8;
    private static final int DEBUFF = 5;

    public GiantHunt() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = DEBUFF;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p == null || m == null) {
            return;
        }
        boolean targetIsAtLeastAsLarge = m.maxHealth >= p.maxHealth;
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (targetIsAtLeastAsLarge) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new WeakPower((AbstractCreature)m, this.magicNumber, false), this.magicNumber));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PoiseBreakPower((AbstractCreature)m, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GiantHunt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
