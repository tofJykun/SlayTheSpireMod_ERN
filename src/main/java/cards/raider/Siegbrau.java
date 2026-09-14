package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;
import relics.FighterDestined;

public class Siegbrau extends CustomCard {
    public static final String ID = "Siegbrau";
    private static final String IMG_PATH = "img/cards/raider/Siegbrau.png";
    private static final int COST = 1;
    private static final int REDUCTION = 6;
    private static final int UPGRADE_PLUS_REDUCTION = 3;

    public Siegbrau() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = REDUCTION;
        this.magicNumber = this.baseMagicNumber;
        this.selfRetain = true;
        this.exhaust = true;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        reduceGreyHealth(p, hasFighterDestined(p) ? GreyHealthPlusPower.POWER_ID : GreyHealthPower.POWER_ID);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        refreshDescription();
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

    private void refreshDescription() {
        CardStrings cardStrings = getCardStrings();
        String description = hasFighterDestined(AbstractDungeon.player)
                ? cardStrings.EXTENDED_DESCRIPTION[0]
                : cardStrings.DESCRIPTION;
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    private static boolean hasFighterDestined(AbstractPlayer player) {
        return player != null && player.hasRelic(FighterDestined.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Siegbrau();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_REDUCTION);
            initializeDescription();
        }
    }
}
