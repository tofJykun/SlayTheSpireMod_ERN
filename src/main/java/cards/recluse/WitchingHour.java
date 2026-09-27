package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.FadingIntelligencePower;
import powers.FrostbitePower;
import powers.IntelligencePower;
import powers.SleepPower;

public class WitchingHour extends CustomCard {
    public static final String ID = "WitchingHour";
    private static final String IMG_PATH = "img/cards/recluse/WitchingHour.png";
    private static final int COST = 0;
    private static final int EFFECT_AMOUNT = 1;
    private static final int UPGRADE_PLUS_EFFECT_AMOUNT = 1;

    public WitchingHour() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = EFFECT_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new IntelligencePower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p,
                new FadingIntelligencePower(p, this.magicNumber), this.magicNumber));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p,
                        new FrostbitePower(monster, this.magicNumber), this.magicNumber,
                        AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(monster, p,
                        new SleepPower(monster, this.magicNumber), this.magicNumber,
                        AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WitchingHour();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT_AMOUNT);
        }
    }
}
