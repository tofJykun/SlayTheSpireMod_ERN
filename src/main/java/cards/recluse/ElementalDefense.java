package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ElementalDefensePower;

public class ElementalDefense extends CustomCard {
    public static final String ID = "ElementalDefense";
    private static final String IMG_PATH = "img/cards/recluse/ElementalDefense.png";
    private static final int COST = 2;
    private static final int COST_UPGRADE = 1;
    private static final int COST_REDUCTION = 1;

    public ElementalDefense() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.POWER,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new ElementalDefensePower((AbstractCreature)p, COST_REDUCTION), COST_REDUCTION));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new ElementalDefense();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(COST_UPGRADE);
        }
    }
}
