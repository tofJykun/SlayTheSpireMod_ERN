package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PosionedReliefPower;

public class PosionedRelief extends CustomCard {
    public static final String ID = "PosionedRelief";

    public PosionedRelief() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/PosionedRelief.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseMagicNumber = this.magicNumber = 15;
    }

    public int getDelayedHpLoss() {
        return this.upgraded ? 40 : 25;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster == null || monster.isDeadOrEscaped() || monster.halfDead || monster.currentHealth <= 0) {
            return;
        }
        addToBot(new HealAction(monster, player, this.magicNumber));
        addToBot(new ApplyPowerAction(player, player,
                new PosionedReliefPower(player, monster, getDelayedHpLoss()), getDelayedHpLoss()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(10);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PosionedRelief();
    }
}
