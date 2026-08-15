package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingHelper;
import patches.AbstractCardEnum;

public class PrismStone extends CustomCard {
    public static final String ID = "PrismStone";
    private static final String IMG_PATH = "img/cards/raider/PrismStone.png";
    private static final int COST = 1;

    public PrismStone() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard material = SmithingHelper.randomSmithingMaterial();
        if (material != null) {
            addToBot((AbstractGameAction)new MakeTempCardInHandAction(material, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PrismStone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
