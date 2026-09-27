package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;

public class Slam extends CustomCard {
    public static final String ID = "Slam";
    private static final String IMG_PATH = "img/cards/undertaker/Slam.png";
    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int POISE_BREAK = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_POISE_BREAK = 1;

    public Slam() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.baseMagicNumber = this.magicNumber = POISE_BREAK;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p,
                        new PoiseBreakPower(monster, this.magicNumber), this.magicNumber));
            }
        }
        addToBot(new DiscardAction(p, p, 1, !this.upgraded));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_POISE_BREAK);
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Slam();
    }
}
