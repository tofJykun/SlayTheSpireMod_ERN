package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;
import patches.ReplayField;

public class ScavengerCurvedSword extends CustomCard {
    public static final String ID = "ScavengerCurvedSword";
    private static final String IMG_PATH = "img/cards/duchess/ScavengerCurvedSword.png";
    private static final int COST = 1;
    private static final int DEBUFF_AMOUNT = 1;
    private static final int REPLAY = 1;

    public ScavengerCurvedSword() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseMagicNumber = DEBUFF_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        ReplayField.setReplay(this, REPLAY);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                new VulnerablePower((AbstractCreature)m, this.magicNumber, false), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                new WeakPower((AbstractCreature)m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScavengerCurvedSword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            ReplayField.setReplay(this, 2);
            initializeDescription();
        }
    }
}
