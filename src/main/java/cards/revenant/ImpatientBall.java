package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ImpatientBallPower;
import powers.SpiritPower;

public class ImpatientBall extends CustomCard {
    public static final String ID = "ImpatientBall";
    private static final String IMG_PATH = "img/cards/revenant/ImpatientBall.png";
    private static final int COST = 1;
    private static final int SPIRIT = 6;
    private static final int SPIRIT_LOSS = 1;
    private static final int UPGRADE_DRAW = 2;

    public ImpatientBall() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = SPIRIT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SpiritPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new ImpatientBallPower((AbstractCreature)p, SPIRIT_LOSS), SPIRIT_LOSS));
        if (this.upgraded) {
            addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, UPGRADE_DRAW));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ImpatientBall();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
