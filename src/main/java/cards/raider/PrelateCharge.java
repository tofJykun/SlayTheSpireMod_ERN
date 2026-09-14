package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;
import relics.FighterDestined;

public class PrelateCharge extends CustomCard {
    public static final String ID = "PrelateCharge";
    private static final String IMG_PATH = "img/cards/raider/PrelateCharge.png";
    private static final int COST = -1;
    private static final int DAMAGE = 3;
    private static final int GREY_HEALTH_THRESHOLD = 3;

    public PrelateCharge() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = GREY_HEALTH_THRESHOLD;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p == null) {
            return;
        }
        int x = getXValue(p);
        final int threshold = this.magicNumber;
        if (m != null) {
            for (int i = 0; i < x; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
        if (x > 0) {
            addToBot(new DrawCardAction(p, 2 * x));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                String powerId = hasFighterDestined(p) ? GreyHealthPlusPower.POWER_ID : GreyHealthPower.POWER_ID;
                AbstractPower power = p.getPower(powerId);
                if (power != null && power.amount >= threshold) {
                    addToTop(new GainEnergyAction(1));
                }
                this.isDone = true;
            }
        });
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    private int getXValue(AbstractPlayer p) {
        int value = this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            value += 2;
            p.getRelic("Chemical X").flash();
        }
        return Math.max(0, value);
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

    private void refreshDescription() {
        CardStrings strings = getCardStrings();
        String description = hasFighterDestined(AbstractDungeon.player)
                ? strings.EXTENDED_DESCRIPTION[0] : strings.DESCRIPTION;
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
        return new PrelateCharge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(-1);
            refreshDescription();
        }
    }
}
