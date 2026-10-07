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
import powers.HoverPower;
import powers.PerfectGuardPower;

public class WingCrestShield extends CustomCard {
    public static final String ID = "WingCrestShield";
    public static final int HOVER = 1;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public WingCrestShield() {
        super(ID, STRINGS.NAME, "img/cards/guardian/WingCrestShield.png", 2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 12;
        baseMagicNumber = magicNumber = 2;
    }

    public int getHoverAmount() {
        return upgraded ? 2 : HOVER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new PerfectGuardPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new HoverPower(p, getHoverAmount()), getHoverAmount()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(2);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WingCrestShield();
    }
}
