package cards.recluse;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;

public class CannonOfHaima extends AbstractScheduledCard {
    public static final String ID = "CannonOfHaima";
    private static final String IMG_PATH = "img/cards/recluse/CannonOfHaima.png";
    private static final int COST = 3;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DAMAGE = 5;
    private static final int POISE_BREAK = 2;
    private static final int UPGRADE_PLUS_POISE_BREAK = 1;
    private static final int DEFAULT_SCHEDULED = 4;

    public CannonOfHaima() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY,
                DEFAULT_SCHEDULED);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.baseMagicNumber = this.magicNumber = POISE_BREAK;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage,
                this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p,
                        new PoiseBreakPower(monster, this.magicNumber), this.magicNumber,
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CannonOfHaima();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_POISE_BREAK);
        }
    }
}
