package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.MadnessPower;

public class FrenzyflameThrust extends CustomCard {
    public static final String ID = "FrenzyflameThrust";

    public FrenzyflameThrust() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/FrenzyflameThrust.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public int getDrawAmount() {
        return this.upgraded ? 2 : 1;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster != null) {
            addToBot(new ApplyPowerAction(monster, player,
                    new MadnessPower(monster, this.magicNumber), this.magicNumber));
        }
        addToBot(new DrawCardAction(player, getDrawAmount()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FrenzyflameThrust();
    }
}
