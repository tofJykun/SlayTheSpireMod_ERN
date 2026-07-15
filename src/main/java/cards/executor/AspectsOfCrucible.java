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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.BloodlossPower;
import powers.FrostbitePower;
import powers.MadnessPower;
import powers.SleepPower;

public class AspectsOfCrucible extends CustomCard {
    public static final String ID = "AspectsOfCrucible";
    private static final String IMG_PATH = "img/cards/executor/AspectsOfCrucible.png";
    private static final int COST = 2;

    public AspectsOfCrucible() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature player = (AbstractCreature)p;
        convertPower(player, FrostbitePower.POWER_ID, new StrengthPower(player, getPowerAmount(player, FrostbitePower.POWER_ID)));
        convertPower(player, SleepPower.POWER_ID, new DexterityPower(player, getPowerAmount(player, SleepPower.POWER_ID)));
        convertPower(player, MadnessPower.POWER_ID, new RetainCardPower(player, getPowerAmount(player, MadnessPower.POWER_ID)));
        convertPower(player, BloodlossPower.POWER_ID, new MetallicizePower(player, getPowerAmount(player, BloodlossPower.POWER_ID)));
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
        return new AspectsOfCrucible();
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
