package cards.executor;

import actions.LetFeastBeginAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class LetFeastBegin extends CustomCard {
    public static final String ID = "LetFeastBegin";
    private static final String IMG_PATH = "img/cards/executor/DestinedDeath.png";
    private static final int COST = 1;
    private static final int MAX_HP_GAIN = 3;
    private static final int UPGRADE_PLUS_MAX_HP_GAIN = 2;

    public LetFeastBegin() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = MAX_HP_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new LetFeastBeginAction(p, m, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LetFeastBegin();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAX_HP_GAIN);
            initializeDescription();
        }
    }
}
