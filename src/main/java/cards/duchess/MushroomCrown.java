package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.MushroomCrownPower;

public class MushroomCrown extends AbstractScheduledCard {
    public static final String ID = "MushroomCrown";
    private static final String IMG_PATH = "img/cards/duchess/MushroomCrown.png";
    private static final int COST = 2;
    private static final int BLOCK = 12;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int MUSHROOMS = 1;
    private static final int UPGRADE_PLUS_MUSHROOMS = 1;
    private static final int DEFAULT_SCHEDULED = 3;

    public MushroomCrown() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF,
                DEFAULT_SCHEDULED);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = MUSHROOMS;
        this.magicNumber = this.baseMagicNumber;
        onScheduledCountChanged(DEFAULT_SCHEDULED, DEFAULT_SCHEDULED);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction(p, p, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new MushroomCrownPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = MUSHROOMS;
        this.magicNumber = this.upgraded ? MUSHROOMS + UPGRADE_PLUS_MUSHROOMS : MUSHROOMS;
    }

    @Override
    public AbstractCard makeCopy() {
        return new MushroomCrown();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MUSHROOMS);
        }
    }
}
