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
import powers.BellowingDragoncrestPower;

public class BellowingDragoncrest extends CustomCard {
    public static final String ID = "BellowingDragoncrest";
    private static final String IMG_PATH = "img/cards/recluse/BellowingDragoncrest.png";
    private static final int COST = 2;
    private static final int INTELLIGENCE = 1;

    public BellowingDragoncrest() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.POWER,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = INTELLIGENCE;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new BellowingDragoncrestPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new BellowingDragoncrest();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(COST - 1);
        }
    }
}
