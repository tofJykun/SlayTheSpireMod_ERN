package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;
import powers.BloodlossPower;
import powers.FrostbitePower;
import powers.MadnessPower;
import powers.SleepPower;

public class Numbness extends CustomCard {
    public static final String ID = "Numbness";
    private static final String IMG_PATH = "img/cards/executor/Numbness.png";
    private static final int COST = 1;

    public Numbness() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature player = (AbstractCreature)p;
        convertPower(player, FrostbitePower.POWER_ID,
                new VulnerablePower(player, getPowerAmount(player, FrostbitePower.POWER_ID), false));
        convertPower(player, SleepPower.POWER_ID,
                new FrailPower(player, getPowerAmount(player, SleepPower.POWER_ID), false));
        convertPower(player, MadnessPower.POWER_ID,
                new WeakPower(player, getPowerAmount(player, MadnessPower.POWER_ID), false));
        convertPower(player, BloodlossPower.POWER_ID,
                new WeakPower(player, getPowerAmount(player, BloodlossPower.POWER_ID), false));
    }

    private static void convertPower(AbstractCreature player, String sourcePowerId, AbstractPower targetPower) {
        int amount = getPowerAmount(player, sourcePowerId);
        if (amount <= 0) {
            return;
        }
        addToBotStatic((AbstractGameAction)new RemoveSpecificPowerAction(player, player, sourcePowerId));
        addToBotStatic((AbstractGameAction)new ApplyPowerAction(player, player, targetPower, amount));
    }

    private static int getPowerAmount(AbstractCreature player, String powerId) {
        AbstractPower power = player.getPower(powerId);
        return power == null ? 0 : power.amount;
    }

    private static void addToBotStatic(AbstractGameAction action) {
        com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(action);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Numbness();
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
