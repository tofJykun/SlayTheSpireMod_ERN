package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class DancerEnchantedSwords extends AbstractScheduledCard {
    public static final String ID = "DancerEnchantedSwords";
    private static final String IMG_PATH = "img/cards/duchess/DancerEnchantedSwords.png";
    private static final int COST = -1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int DEFAULT_SCHEDULED = 3;

    public DancerEnchantedSwords() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY,
                DEFAULT_SCHEDULED);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        onScheduledCountChanged(DEFAULT_SCHEDULED, DEFAULT_SCHEDULED);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = getXValue(p, true);
        for (int i = 0; i < count; i++) {
            addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage,
                    this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
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
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = base;
        this.magicNumber = current;
        this.isMagicNumberModified = current != base;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DancerEnchantedSwords();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
