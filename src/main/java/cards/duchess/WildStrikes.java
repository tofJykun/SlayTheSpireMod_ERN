package cards.duchess;

import actions.WildStrikesRepeatAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class WildStrikes extends CustomCard {
    public static final String ID = "WildStrikes";
    private static final String IMG_PATH = "img/cards/duchess/WildStrikes.png";
    private static final int COST = -1;
    private static final int DAMAGE = 9;
    private int remainingPlays = -1;

    public WildStrikes() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public void setRemainingPlays(int remainingPlays) {
        this.remainingPlays = remainingPlays;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (!this.upgraded && getXValue(p, false) <= 0) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int plays = this.remainingPlays >= 0 ? this.remainingPlays : getPlayCount(p, true);
        this.remainingPlays = -1;
        if (plays <= 0) {
            return;
        }

        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot((AbstractGameAction)new WildStrikesRepeatAction(this, m, plays - 1));
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    private int getPlayCount(AbstractPlayer p, boolean flashChemicalX) {
        return getXValue(p, flashChemicalX) + (this.upgraded ? 1 : 0);
    }

    private int getXValue(AbstractPlayer p, boolean flashChemicalX) {
        int value = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            value = this.energyOnUse;
        }
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
            if (flashChemicalX) {
                p.getRelic("Chemical X").flash();
            }
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new WildStrikes();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
