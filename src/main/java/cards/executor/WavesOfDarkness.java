package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.WavesOfDarknessPower;

public class WavesOfDarkness extends CustomCard {
    public static final String ID = "WavesOfDarkness";
    public static final int DRAW = 1;

    public WavesOfDarkness() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/WavesOfDarkness.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION.replace("!MD!", Integer.toString(DRAW)),
                CardType.POWER, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WavesOfDarknessPower(p, this.magicNumber, DRAW), 0));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(-1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WavesOfDarkness();
    }
}
