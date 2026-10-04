package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class SwordOfNightAndFlame extends CustomCard {
    public static final String ID = "SwordOfNightAndFlame";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private int areaDamage = 12;

    public SwordOfNightAndFlame() {
        super(ID, STRINGS.NAME, "img/cards/revenant/SwordOfNightAndFlame.png", 0, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 4;
        exhaust = true;
    }

    public int getBaseAreaDamage() {
        return upgraded ? 15 : 12;
    }

    public int getAreaDamage() {
        return areaDamage;
    }

    @Override
    public void applyPowers() {
        int singleBase = baseDamage;
        try {
            baseDamage = getBaseAreaDamage();
            super.applyPowers();
            areaDamage = damage;
        } finally {
            baseDamage = singleBase;
        }
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        if (m == null) {
            applyPowers();
            return;
        }
        int singleBase = baseDamage;
        // Calculate the area hit for every enemy, then restore the selected-target hit.
        try {
            baseDamage = getBaseAreaDamage();
            isMultiDamage = true;
            super.calculateCardDamage(m);
            int targetIndex = AbstractDungeon.getMonsters().monsters.indexOf(m);
            areaDamage = targetIndex >= 0 ? multiDamage[targetIndex] : damage;
        } finally {
            baseDamage = singleBase;
            isMultiDamage = false;
        }
        super.calculateCardDamage(m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        calculateCardDamage(m);
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        addToBot(new DamageAllEnemiesAction(p, multiDamage.clone(), damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (EnergyPanel.totalCount != 0) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = EnergyPanel.totalCount == 0
                ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        areaDamage = getBaseAreaDamage();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        SwordOfNightAndFlame copy = (SwordOfNightAndFlame)super.makeStatEquivalentCopy();
        copy.areaDamage = areaDamage;
        return copy;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            areaDamage = getBaseAreaDamage();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordOfNightAndFlame();
    }
}
