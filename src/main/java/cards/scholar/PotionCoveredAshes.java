package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.BulkPotionQueue;
import patches.AbstractCardEnum;

public class PotionCoveredAshes extends CustomCard {
    public static final String ID = "PotionCoveredAshes";
    private static final String IMG_PATH = "img/cards/scholar/Defend.png";
    private static final int COST = 1;

    public PotionCoveredAshes() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.BASIC, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ObtainPotionAction(BulkPotionQueue.getRandomBulkPotion()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PotionCoveredAshes();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
