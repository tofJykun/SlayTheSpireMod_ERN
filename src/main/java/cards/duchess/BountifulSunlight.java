package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ScheduledField;
import java.util.Set;

public class BountifulSunlight extends AbstractScheduledCard {
    public static final String ID = "BountifulSunlight";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private int playsSinceGrowth;

    public BountifulSunlight() {
        super(ID, STRINGS.NAME, "img/cards/duchess/BountifulSunlight.png", -2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.SELF, 2);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (ScheduledField.isPendingAutoplay(this)) return super.canUse(p, m);
        this.cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(2));
        Set<AbstractCard> instances = GetAllInBattleInstances.get(this.uuid);
        instances.add(this);
        int count = this.playsSinceGrowth;
        int base = ScheduledField.getBaseScheduled(this);
        for (AbstractCard card : instances) {
            if (card instanceof BountifulSunlight) {
                count = Math.max(count, ((BountifulSunlight)card).playsSinceGrowth);
                base = Math.max(base, ScheduledField.getBaseScheduled(card));
            }
        }
        boolean grow = ++count >= this.magicNumber;
        if (grow) {
            count = 0;
            if (base < Integer.MAX_VALUE) base++;
        }
        // Same-UUID replays share combat growth, but never modify the master deck.
        for (AbstractCard card : instances) {
            if (card instanceof BountifulSunlight) {
                ((BountifulSunlight)card).playsSinceGrowth = count;
                if (grow) ScheduledField.setBaseScheduled(card, base);
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        BountifulSunlight copy = (BountifulSunlight)super.makeStatEquivalentCopy();
        copy.playsSinceGrowth = this.playsSinceGrowth;
        return copy;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BountifulSunlight();
    }
}
