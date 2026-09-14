package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class ChaosBlade extends CustomCard {
    public static final String ID = "ChaosBlade";
    private static final String IMG_PATH = "img/cards/executor/ChaosBlade.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 15;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int ABERRATION = 2;

    public ChaosBlade() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = ABERRATION;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        AbstractPower power = getRandomAberration(p);
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                power, this.magicNumber, AbstractGameAction.AttackEffect.NONE));
    }

    private AbstractPower getRandomAberration(AbstractPlayer p) {
        switch (AbstractDungeon.cardRandomRng.random(3)) {
            case 0:
                return new FrostbitePower((AbstractCreature)p, this.magicNumber);
            case 1:
                return new SleepPower((AbstractCreature)p, this.magicNumber);
            case 2:
                return new MadnessPower((AbstractCreature)p, this.magicNumber);
            default:
                return new BloodlossPower((AbstractCreature)p, this.magicNumber);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChaosBlade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
