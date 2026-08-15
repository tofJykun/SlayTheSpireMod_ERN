package cards.wylder;

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
import powers.CeruleanDaggerPower;

public class CeruleanDagger extends CustomCard {
    public static final String ID = "CeruleanDagger";
    private static final String IMG_PATH = "img/cards/wylder/CeruleanDagger.png";
    private static final int COST = 1;
    private static final int POWER_FLAG = 1;
    private static final int UPGRADED_POWER_FLAG = 2;

    public CeruleanDagger() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int stackFlag = this.upgraded ? UPGRADED_POWER_FLAG : POWER_FLAG;
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new CeruleanDaggerPower((AbstractCreature)p, this.upgraded), stackFlag));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CeruleanDagger();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
