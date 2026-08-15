package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;

public class BleedStone extends CustomCard {
    public static final String ID = "BleedStone";
    private static final String IMG_PATH = "img/cards/raider/BleedStone.png";
    private static final int COST = -2;
    private static final int REDUCTION = 5;
    private static final int UPGRADE_PLUS_REDUCTION = 2;

    public BleedStone() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = REDUCTION;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        reduceGreyHealth(p, GreyHealthPower.POWER_ID);
        reduceGreyHealth(p, GreyHealthPlusPower.POWER_ID);
    }

    private void reduceGreyHealth(AbstractPlayer p, String powerId) {
        AbstractPower power = p.getPower(powerId);
        if (power == null) {
            return;
        }
        if (power.amount <= this.magicNumber) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction((AbstractCreature)p,
                    (AbstractCreature)p, power));
        } else {
            addToBot((AbstractGameAction)new ReducePowerAction((AbstractCreature)p,
                    (AbstractCreature)p, power, this.magicNumber));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BleedStone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_REDUCTION);
        }
    }
}
