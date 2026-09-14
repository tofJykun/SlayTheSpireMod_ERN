package cards.scholar;

import actions.MiniatureStraghessAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class MiniatureStraghess extends CustomCard {
    public static final String ID = "MiniatureStraghess";
    private static final String IMG_PATH = "img/cards/scholar/MiniatureStraghess.png";
    private static final int COST = 3;

    public MiniatureStraghess() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MiniatureStraghessAction(m));
    }

    @Override
    public MiniatureStraghess makeCopy() {
        return new MiniatureStraghess();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }
}
