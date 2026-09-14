package cards.raider;

import cards.AbstractScheduledCard;
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
import powers.PoiseBreakPower;

public class StormKick extends AbstractScheduledCard {
    public static final String ID = "StormKick";
    private static final String IMG_PATH = "img/cards/raider/StormKick.png";
    private static final int COST = 3;
    private static final int DAMAGE = 2;
    private static final int DEFAULT_SCHEDULED = 3;
    private int poiseBreak = 1;

    public StormKick() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY,
                DEFAULT_SCHEDULED);
        this.baseDamage = DAMAGE;
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
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(m, p, new PoiseBreakPower(m, this.poiseBreak), this.poiseBreak));
    }

    @Override
    public void onScheduledCountChanged(int current, int base) {
        this.baseMagicNumber = base;
        this.magicNumber = current;
        this.isMagicNumberModified = current != base;
    }

    private void refreshDescription() {
        this.rawDescription = getCardStrings().DESCRIPTION.replace("!MX!", Integer.toString(this.poiseBreak));
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new StormKick();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(1);
            this.poiseBreak += 1;
            refreshDescription();
        }
    }
}
