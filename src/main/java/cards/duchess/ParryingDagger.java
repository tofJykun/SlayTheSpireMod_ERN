package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;

public class ParryingDagger extends AbstractScheduledCard {
    public static final String ID = "ParryingDagger";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public ParryingDagger() {
        super(ID, STRINGS.NAME, "img/cards/duchess/ParryingDagger.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.SELF, 3);
        this.baseBlock = 8;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(2);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParryingDagger();
    }
}
