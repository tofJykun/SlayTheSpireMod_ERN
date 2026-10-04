package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import powers.PoiseBreakPower;

public class AstoraGreatsword extends CustomCard {
    public static final String ID = "AstoraGreatsword";
    private static final String IMG_PATH = "img/cards/wylder/AstoraGreatsword.png";
    private static final int COST = -1;
    private static final int DAMAGE = 9;
    private static final int POISE_BREAK = 1;
    private static final int UPGRADE_PLUS_POISE_BREAK = 1;

    public AstoraGreatsword() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = POISE_BREAK;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = getXValue(p) + (this.upgraded ? 1 : 0);
        if (count > 0 && m != null) {
            for (int i = 0; i < count; i++) {
                addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                        new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                        new PoiseBreakPower((AbstractCreature)m, this.magicNumber),
                        this.magicNumber, AbstractGameAction.AttackEffect.NONE));
            }
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
        return new AstoraGreatsword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISE_BREAK);
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
