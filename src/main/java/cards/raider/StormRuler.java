package cards.raider;

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
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;

public class StormRuler extends CustomCard {
    public static final String ID = "StormRuler";
    private static final String IMG_PATH = "img/cards/raider/StormRuler.png";
    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int POISE_BREAK = 3;
    private static final int UPGRADE_PLUS_POISE_BREAK = 1;
    private static final int HEALTH_THRESHOLD = 50;

    public StormRuler() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = POISE_BREAK;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        boolean highHealth = m.currentHealth >= HEALTH_THRESHOLD;
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                new PoiseBreakPower((AbstractCreature)m, this.magicNumber), this.magicNumber));
        if (highHealth) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PoiseBreakPower((AbstractCreature)m, this.magicNumber), this.magicNumber));
        }
    }

    private void refreshDescription() {
        String description = getCardStrings().DESCRIPTION
                .replace("!MX!", Integer.toString(this.magicNumber))
                .replace("!M!", Integer.toString(HEALTH_THRESHOLD));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StormRuler();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISE_BREAK);
            refreshDescription();
        }
    }
}
