package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.BulkPotionQueue;
import general.CrudeDrug;
import patches.AbstractCardEnum;
import patches.FragileField;
import java.util.List;

public class Sephirot extends CustomCard {
    public static final String ID = "Sephirot";

    public Sephirot() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/Sephirot.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        FragileField.setBase(this, 10);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ObtainPotionAction(BulkPotionQueue.getRandomBulkPotion()));
        }
        List<String> candidates = CrudeDrug.getCrudeDrugCardIds();
        if (candidates.isEmpty()) return;
        for (int i = 0; i < magicNumber; i++) {
            String id = candidates.get(AbstractDungeon.cardRandomRng.random(candidates.size() - 1));
            addToBot(new MakeTempCardInHandAction(CardLibrary.getCopy(id), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override
    public AbstractCard makeCopy() { return new Sephirot(); }
}
