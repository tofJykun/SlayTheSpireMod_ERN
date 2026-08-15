package cards.undertaker;

import actions.IncreaseCostAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import patches.AbstractCardEnum;

public class WhatsUp extends CustomCard {
    public static final String ID = "WhatsUp";
    private static final String IMG_PATH = "img/cards/undertaker/WhatsUp.png";
    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int INTANGIBLE = 1;
    private static final int COST_INCREASE = 1;

    public WhatsUp() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = COST_INCREASE;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new IntangiblePlayerPower((AbstractCreature)p, INTANGIBLE), INTANGIBLE));
        addToBot((AbstractGameAction)new IncreaseCostAction(this.uuid, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhatsUp();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
