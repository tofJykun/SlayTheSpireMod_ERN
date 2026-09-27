package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.AllKnowingHelmPower;

public class AllKnowingHelm extends CustomCard {
    public static final String ID = "AllKnowingHelm";
    private static final String IMG_PATH = "img/cards/undertaker/AllKnowingHelm.png";
    private static final int COST = 3;
    private static final int PLAY_COUNT = 5;
    private static final int UPGRADE_PLUS_PLAY_COUNT = 3;

    public AllKnowingHelm() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = PLAY_COUNT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new AllKnowingHelmPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AllKnowingHelm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PLAY_COUNT);
        }
    }
}
