package cards.duchess;

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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;
import powers.HornetRingPower;

public class AssassinGambit extends CustomCard {
    public static final String ID = "AssassinGambit";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/duchess/AssassinGambit.png";
    private static final int COST = 1;
    private static final int STAT_GAIN = 3;
    private static final int UPGRADED_STAT_GAIN = 4;

    public AssassinGambit() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = STAT_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && !HornetRingPower.isAttackIntent(m.intent)) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new StrengthPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new VigorPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AssassinGambit();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_STAT_GAIN - STAT_GAIN);
        }
    }
}
