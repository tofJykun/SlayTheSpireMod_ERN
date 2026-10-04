package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.GreatshieldOfGloryPower;
import powers.PerfectGuardPower;

public class GreatshieldOfGlory extends CustomCard {
    public static final String ID = "GreatshieldOfGlory";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public GreatshieldOfGlory() {
        super(ID, STRINGS.NAME, "img/cards/guardian/GreatshieldOfGlory.png", 2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 12;
        baseMagicNumber = magicNumber = 2;
    }

    public int getVigorAmount() {
        return upgraded ? 4 : 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new PerfectGuardPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new GreatshieldOfGloryPower(p, getVigorAmount()), getVigorAmount()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(4);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreatshieldOfGlory();
    }
}
