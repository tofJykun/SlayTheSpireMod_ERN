package cards.revenant;

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

public class LightningRam extends CustomCard {
    public static final String ID = "LightningRam";
    private static final String IMG_PATH = "img/cards/revenant/LightningRam.png";
    private static final int COST = -1;
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int HIT_MULTIPLIER = 2;

    public LightningRam() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hitCount = getXValue(p) * HIT_MULTIPLIER;
        for (int i = 0; i < hitCount; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.LIGHTNING));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    private int getXValue(AbstractPlayer p) {
        int value = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            value = this.energyOnUse;
        }
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
            p.getRelic("Chemical X").flash();
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightningRam();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
