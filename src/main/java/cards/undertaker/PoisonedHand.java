package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import patches.AbstractCardEnum;
import powers.PostureBreakPower;

public class PoisonedHand extends CustomCard {
    public static final String ID = "PoisonedHand";
    private static final String IMG_PATH = "img/cards/undertaker/PoisonedHand.png";
    private static final int COST = 5;
    private static final int POISON = 10;
    private static final int UPGRADE_PLUS_POISON = 5;

    public PoisonedHand() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE,
                CardTarget.ENEMY);
        this.baseMagicNumber = POISON;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PoisonPower((AbstractCreature)m, (AbstractCreature)p, this.magicNumber),
                    this.magicNumber));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PostureBreakPower(m), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PoisonedHand();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
        }
    }
}
