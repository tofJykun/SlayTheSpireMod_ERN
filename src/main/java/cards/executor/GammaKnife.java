package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;

import java.util.ArrayList;

public class GammaKnife extends CustomCard {
    public static final String ID = "GammaKnife";
    private static final String IMG_PATH = "img/cards/executor/GammaKnife.png";
    private static final int COST = 1;

    public GammaKnife() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs.add(power);
            }
        }

        for (AbstractPower power : debuffs) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction((AbstractCreature)p,
                    (AbstractCreature)p, power.ID));
        }
        if (!debuffs.isEmpty()) {
            addToBot((AbstractGameAction)new GainEnergyAction(debuffs.size()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GammaKnife();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
