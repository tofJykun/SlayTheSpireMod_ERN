package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;

public class CaneSword extends CustomCard {
    public static final String ID = "CaneSword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CaneSword() {
        super(ID, STRINGS.NAME, "img/cards/scholar/CaneSword.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 3;
        FragileField.setBase(this, this.baseMagicNumber);
    }

    @Override
    public void applyPowers() {
        int originalBase = this.baseDamage;
        this.baseDamage += Math.max(0, AbstractDungeon.floorNum);
        try {
            super.applyPowers();
        } finally {
            this.baseDamage = originalBase;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBase = this.baseDamage;
        this.baseDamage += Math.max(0, AbstractDungeon.floorNum);
        try {
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalBase;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
            FragileField.upgradeBase(this, 5);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CaneSword();
    }
}
