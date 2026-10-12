package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.MasterPlannerPower;

public class MasterPlanner extends CustomCard {
    public static final String ID = "MasterPlanner";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public MasterPlanner() {
        super(ID, STRINGS.NAME, "img/cards/duchess/MasterPlanner.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MasterPlannerPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new MasterPlanner(); }
}
