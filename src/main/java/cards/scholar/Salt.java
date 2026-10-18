package cards.scholar;

import basemod.abstracts.CustomCard;
import cards.tempcards.MottledPot;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SaltPower;

public class Salt extends CustomCard {
    public static final String ID = "Salt";
    public static final int MAX_HP_GAIN = 3;
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Salt() {
        super(ID, STRINGS.NAME, "img/cards/scholar/Salt.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        cardsToPreview = new MottledPot();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SaltPower(p), MAX_HP_GAIN));
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override
    public AbstractCard makeCopy() { return new Salt(); }
}
