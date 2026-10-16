package cards.ironeye;

import basemod.abstracts.CustomCard;
import cards.tempcards.LotteryTicket;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.GoldPaymentHelper;
import patches.AbstractCardEnum;

public class CashOnDelivery extends CustomCard {
    public static final String ID = "CashOnDelivery";
    public static final int TICKET_AMOUNT = 1;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CashOnDelivery() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/CashOnDelivery.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 10;
        cardsToPreview = new LotteryTicket();
        cardsToPreview.upgrade();
    }

    public int getDrawAmount() {
        return upgraded ? 4 : 3;
    }

    @Override
    public boolean canUse(AbstractPlayer player, AbstractMonster monster) {
        if (!super.canUse(player, monster)) return false;
        if (player == null || GoldPaymentHelper.available(player) < magicNumber) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        player.loseGold(magicNumber);
        addToBot(new DrawCardAction(player, getDrawAmount()));
        LotteryTicket ticket = new LotteryTicket();
        ticket.upgrade();
        addToBot(new MakeTempCardInHandAction(ticket, TICKET_AMOUNT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CashOnDelivery();
    }
}
