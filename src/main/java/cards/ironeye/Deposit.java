package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import general.GoldPaymentHelper;
import patches.AbstractCardEnum;

public class Deposit extends CustomCard {
    public static final String ID = "Deposit";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int GOLD_LOSS = 10;

    public Deposit() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/Deposit.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = GOLD_LOSS;
    }

    public int getNextTurnAmount() { return upgraded ? 3 : 2; }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (p == null || GoldPaymentHelper.available(p) < GOLD_LOSS) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseGold(magicNumber);
        int amount = getNextTurnAmount();
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, amount), amount));
        addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, amount), amount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) upgradeName();
    }

    @Override
    public AbstractCard makeCopy() { return new Deposit(); }
}
