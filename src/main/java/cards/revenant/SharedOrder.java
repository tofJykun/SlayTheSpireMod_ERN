package cards.revenant;

import actions.SharedOrderAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SharedOrder extends CustomCard {
    public static final String ID = "SharedOrder";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SharedOrder() {
        super(ID, STRINGS.NAME, "img/cards/revenant/SharedOrder.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 7;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new GainBlockAction(player, player, block));
        addToBot(new SharedOrderAction(player));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SharedOrder();
    }
}
