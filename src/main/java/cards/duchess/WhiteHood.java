package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import patches.AbstractCardEnum;
import patches.ScheduledField;

public class WhiteHood extends AbstractScheduledCard {
    public static final String ID = "WhiteHood";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public WhiteHood() {
        super(ID, STRINGS.NAME, "img/cards/duchess/WhiteHood.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF, 4);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BlurPower(p, 1), 1));
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = base;
        this.magicNumber = current;
        this.isMagicNumberModified = current != base;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradedMagicNumber = true;
            ScheduledField.setBaseScheduled(this, 3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhiteHood();
    }
}
