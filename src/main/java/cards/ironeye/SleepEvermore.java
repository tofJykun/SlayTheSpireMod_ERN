package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.EternalSleepPower;

public class SleepEvermore extends CustomCard {
    public static final String ID = "SleepEvermore";
    private static final String IMG_PATH = "img/cards/ironeye/SleepEvermore.png";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SleepEvermore() {
        super(ID, STRINGS.NAME, IMG_PATH, 2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = this.magicNumber = 10;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new DamageAction(player, new DamageInfo(player, this.magicNumber, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE));
        if (monster != null && !monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) {
            addToBot(new ApplyPowerAction(monster, player, new EternalSleepPower(monster), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SleepEvermore();
    }
}
