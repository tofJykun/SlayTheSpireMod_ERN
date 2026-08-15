package cards.scholar;

import actions.AjaRedStoneAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class AjaRedStone extends CustomCard {
    public static final String ID = "AjaRedStone";
    private static final String IMG_PATH = "img/cards/scholar/AjaRedStone.png";
    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;

    public AjaRedStone() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        MultiCardPreview.add(this, true, new RoyalLegacy(), new PenglaiWorship(), new Homunculus());
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new AjaRedStoneAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new AjaRedStone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
