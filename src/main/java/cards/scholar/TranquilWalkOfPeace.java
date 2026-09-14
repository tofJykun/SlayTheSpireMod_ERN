package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.LoseStrengthAtEndOfTurnPower;
import powers.SleepPower;

public class TranquilWalkOfPeace extends CustomCard {
    public static final String ID = "TranquilWalkOfPeace";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/scholar/TranquilWalkOfPeace.png";
    private static final int COST = 0;
    private static final int SLEEP = 2;
    private static final int UPGRADED_SLEEP = 4;
    private static final int STRENGTH = 6;
    private static final int UPGRADED_STRENGTH = 10;

    public int baseStrength;
    public int strength;

    public TranquilWalkOfPeace() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = SLEEP;
        this.magicNumber = this.baseMagicNumber;
        this.baseStrength = STRENGTH;
        this.strength = this.baseStrength;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : com.megacrit.cardcrawl.dungeons.AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new SleepPower((AbstractCreature)monster, this.magicNumber),
                        this.magicNumber, AbstractGameAction.AttackEffect.NONE));
            }
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SleepPower((AbstractCreature)p, this.magicNumber),
                this.magicNumber, AbstractGameAction.AttackEffect.NONE));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StrengthPower((AbstractCreature)p, this.strength), this.strength));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new LoseStrengthAtEndOfTurnPower((AbstractCreature)p, this.strength), this.strength));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.strength = this.baseStrength;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.strength = this.baseStrength;
    }

    @Override
    public AbstractCard makeCopy() {
        return new TranquilWalkOfPeace();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADED_SLEEP;
            this.magicNumber = this.baseMagicNumber;
            this.baseStrength = UPGRADED_STRENGTH;
            this.strength = this.baseStrength;
        }
    }
}
