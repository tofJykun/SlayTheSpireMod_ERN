package cards.scholar;

import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.TreeOfLifeProgress;
import patches.AbstractCardEnum;

@NoPools
public class TreeOfLife extends CustomCard {
    public static final String ID = "TreeOfLife";

    public TreeOfLife() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/TreeOfLife.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) { TreeOfLifeProgress.arm(); }

    @Override public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override public AbstractCard makeCopy() { return new TreeOfLife(); }
}
