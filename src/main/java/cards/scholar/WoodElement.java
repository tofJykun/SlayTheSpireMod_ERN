package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CardElement;
import general.CrudeDrug;
import patches.AbstractCardEnum;
import patches.ElementPatch;

import java.util.List;

public class WoodElement extends CustomCard {
    public static final String ID = "WoodElement";

    public WoodElement() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/WoodElement.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        ElementPatch.Fields.element.set(this, CardElement.ElementWood);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
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
    public AbstractCard makeCopy() { return new WoodElement(); }
}
