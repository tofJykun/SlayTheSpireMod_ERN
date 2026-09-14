package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

public class Metastasis extends CustomCard {
    public static final String ID = "Metastasis";
    private static final String IMG_PATH = "img/cards/executor/Metastasis.png";
    private static final int COST = 2;
    private static final int BLOODBURN = 7;
    private static final int UPGRADE_PLUS_BLOODBURN = 2;

    public Metastasis() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = BLOODBURN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new BloodburnPower((AbstractCreature)m, (AbstractCreature)p, this.magicNumber),
                    this.magicNumber, AbstractGameAction.AttackEffect.POISON));
        }
        AbstractCard copy = makeStatEquivalentCopy();
        copy.cost = 0;
        copy.costForTurn = 0;
        copy.isCostModified = true;
        addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(copy, 1, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Metastasis();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOODBURN);
            initializeDescription();
        }
    }
}
