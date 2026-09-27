package cards.recluse;

import actions.FistfulOfAshAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class FistfulOfAsh extends CustomCard {
    public static final String ID = "FistfulOfAsh";
    private static final String IMG_PATH = "img/cards/recluse/FistfulOfAsh.png";
    private static final int COST = 1;
    private static final int BLOCK_PER_STATUS = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public FistfulOfAsh() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = BLOCK_PER_STATUS;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FistfulOfAshAction(p, p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FistfulOfAsh();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
