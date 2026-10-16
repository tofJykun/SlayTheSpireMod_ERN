package cards.ironeye;

import basemod.abstracts.CustomCard;
import cards.tempcards.LotteryTicket;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.GoldPaymentHelper;
import patches.AbstractCardEnum;

public class FortuneCookie extends CustomCard {
    public static final String ID = "FortuneCookie";
    public static final int TICKET_AMOUNT = 1;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public FortuneCookie() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/FortuneCookie.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 9;
        baseMagicNumber = magicNumber = 10;
        cardsToPreview = new LotteryTicket();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (p == null || GoldPaymentHelper.available(p) < magicNumber) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseGold(magicNumber);
        addToBot(new GainBlockAction(p, p, block));
        LotteryTicket ticket = new LotteryTicket();
        if (upgraded) ticket.upgrade();
        addToBot(new MakeTempCardInHandAction(ticket, TICKET_AMOUNT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(1);
            cardsToPreview.upgrade();
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new FortuneCookie(); }
}
