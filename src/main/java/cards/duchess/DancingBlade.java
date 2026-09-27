package cards.duchess;

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
import patches.AbstractCardEnum;
import powers.DancingBladePower;

public class DancingBlade extends CustomCard {
    public static final String ID = "DancingBlade";
    private static final String IMG_PATH = "img/cards/duchess/DancingBlade.png";
    private static final int COST = 0;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DAMAGE = 5;

    public DancingBlade() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new DancingBladePower((AbstractCreature)p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DancingBlade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
