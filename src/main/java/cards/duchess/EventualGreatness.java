package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.EventualGreatnessPower;

public class EventualGreatness extends CustomCard {
    public static final String ID = "EventualGreatness";
    private static final String IMG_PATH = "img/cards/duchess/EventualGreatness.png";
    private static final int COST = 1;
    private static final int STAT_LOSS = 2;
    private static final int CARD_THRESHOLD = 2;
    private static final int UPGRADED_CARD_THRESHOLD = 1;

    public EventualGreatness() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = CARD_THRESHOLD;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getStatLoss() {
        return STAT_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -STAT_LOSS), -STAT_LOSS));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -STAT_LOSS), -STAT_LOSS));
        addToBot(new ApplyPowerAction(p, p, new EventualGreatnessPower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new EventualGreatness();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADED_CARD_THRESHOLD;
            this.magicNumber = this.baseMagicNumber;
            initializeDescription();
        }
    }
}
