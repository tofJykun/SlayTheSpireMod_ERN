package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.CrystalEnchantedPower;

public class CrystalEnchanted extends CustomCard {
    public static final String ID = "CrystalEnchanted";
    private static final String IMG_PATH = "img/cards/duchess/CrystalEnchanted.png";
    private static final int COST = 2;
    private static final int STAT_GAIN = 4;
    private static final int CARD_THRESHOLD = 3;

    public CrystalEnchanted() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = CARD_THRESHOLD;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getStatGain() {
        return STAT_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, STAT_GAIN), STAT_GAIN));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, STAT_GAIN), STAT_GAIN));
        addToBot(new ApplyPowerAction(p, p, new CrystalEnchantedPower(p, this.magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrystalEnchanted();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
