package cards.recluse;

import actions.FistfulOfAshAction;
import basemod.abstracts.CustomCard;
import cards.status.MagicEmber;
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
    private static final int BLOCK_PER_EMBER = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public FistfulOfAsh() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_PER_EMBER;
        this.cardsToPreview = new MagicEmber();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FistfulOfAshAction(p, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FistfulOfAsh();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
