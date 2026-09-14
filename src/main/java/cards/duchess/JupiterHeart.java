package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ScheduledField;
import powers.JupiterHeartPower;

public class JupiterHeart extends AbstractScheduledCard {
    public static final String ID = "JupiterHeart";
    private static final String IMG_PATH = "img/cards/duchess/JupiterHeart.png";
    private static final int COST = -2;
    private static final int EXTRA_TURNS = 1;
    private static final int DEFAULT_SCHEDULED = 12;

    public JupiterHeart() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF,
                DEFAULT_SCHEDULED);
        this.exhaust = true;
        onScheduledCountChanged(DEFAULT_SCHEDULED, DEFAULT_SCHEDULED);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new JupiterHeartPower((AbstractCreature)p, EXTRA_TURNS), EXTRA_TURNS));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (ScheduledField.isPendingAutoplay(this)) {
            return super.canUse(p, m);
        }
        this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = base;
        this.magicNumber = current;
        this.isMagicNumberModified = current != base;
    }

    @Override
    public AbstractCard makeCopy() {
        return new JupiterHeart();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
