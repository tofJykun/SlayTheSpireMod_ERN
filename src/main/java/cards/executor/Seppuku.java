package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.BloodlossPower;
import powers.FrostbitePower;
import powers.MadnessPower;
import powers.SleepPower;

public class Seppuku extends CustomCard {
    public static final String ID = "Seppuku";
    private static final String IMG_PATH = "img/cards/executor/Seppuku.png";
    private static final int COST = 0;
    private static final int ENERGY = 2;
    private static final int UPGRADED_ENERGY = 3;
    private static final int ABERRATION = 3;

    public Seppuku() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = ENERGY;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainEnergyAction(this.magicNumber));
        AbstractPower power = getRandomAberration(p);
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                power, ABERRATION, AbstractGameAction.AttackEffect.NONE));
    }

    private static AbstractPower getRandomAberration(AbstractPlayer p) {
        switch (AbstractDungeon.cardRandomRng.random(3)) {
            case 0:
                return new FrostbitePower((AbstractCreature)p, ABERRATION);
            case 1:
                return new SleepPower((AbstractCreature)p, ABERRATION);
            case 2:
                return new MadnessPower((AbstractCreature)p, ABERRATION);
            default:
                return new BloodlossPower((AbstractCreature)p, ABERRATION);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Seppuku();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADED_ENERGY;
            this.magicNumber = this.baseMagicNumber;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
