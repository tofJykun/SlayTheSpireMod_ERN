package cards.wylder;

import actions.HoneBladeAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class HoneBlade extends CustomCard {
    public static final String ID = "HoneBlade";
    private static final String IMG_PATH = "img/cards/wylder/HoneBlade.png";

    public HoneBlade() {
        super(ID, strings().NAME, IMG_PATH, 1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 5;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new HoneBladeAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(2);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HoneBlade();
    }
}
