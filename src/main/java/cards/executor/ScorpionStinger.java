package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ScarletRotPower;

public class ScorpionStinger extends CustomCard {
    public static final String ID = "ScorpionStinger";

    public ScorpionStinger() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/ScorpionStinger.png", 0,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 3;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    public int getScarletRotAmount() {
        return this.upgraded ? 2 : 1;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster == null) {
            return;
        }
        // Check existing Rot before this card's damage, draw or application can change it.
        boolean draw = monster.hasPower(ScarletRotPower.POWER_ID);
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (draw) {
            addToBot(new DrawCardAction(player, this.magicNumber));
        }
        addToBot(new ApplyPowerAction(monster, player,
                new ScarletRotPower(monster, player, getScarletRotAmount()), getScarletRotAmount()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScorpionStinger();
    }
}
