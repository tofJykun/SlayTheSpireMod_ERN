package cards.scholar;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import patches.AbstractCardEnum;
import patches.ReplayField;

public class LightningPot extends CustomCard {
    public static final String ID = "LightningPot";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private int lastBonus = -1;
    private boolean lastInCombat;

    public LightningPot() {
        super(ID, STRINGS.NAME, "img/cards/scholar/LightningPot.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
        baseMagicNumber = magicNumber = 5;
    }

    private boolean inCombat() { return AbstractDungeon.player != null && CombatState.isInCombat(); }

    private int potionBonus() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.potions == null) return 0;
        int count = 0;
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion != null && !(potion instanceof PotionSlot)) count++;
        }
        return count * magicNumber;
    }

    private void calculate(AbstractMonster monster, boolean targeted) {
        int original = baseDamage;
        lastBonus = potionBonus();
        lastInCombat = inCombat();
        baseDamage += lastBonus;
        try {
            if (!lastInCombat) damage = baseDamage;
            else if (targeted) super.calculateCardDamage(monster);
            else super.applyPowers();
        } finally {
            baseDamage = original;
        }
        isDamageModified = damage != baseDamage;
    }

    @Override public void applyPowers() { calculate(null, false); }
    @Override public void calculateCardDamage(AbstractMonster m) { calculate(m, true); }

    @Override public void update() {
        super.update();
        if (lastBonus != potionBonus() || lastInCombat != inCombat()) applyPowers();
    }

    @Override public void render(SpriteBatch sb, boolean selected) {
        if (!inCombat() || !AbstractDungeon.player.hand.contains(this)) applyPowers();
        super.render(sb, selected);
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
    }

    @Override public boolean canUpgrade() { return timesUpgraded < 2; }

    @Override public void upgrade() {
        if (!canUpgrade()) return;
        upgradeName();
        if (timesUpgraded == 1) {
            upgradeMagicNumber(2);
            upgradedDamage = true;
        } else {
            ReplayField.setReplay(this, 1);
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
        }
        initializeDescription();
        applyPowers();
    }

    @Override public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.applyPowers();
        return copy;
    }

    @Override public AbstractCard makeCopy() { return new LightningPot(); }
}
