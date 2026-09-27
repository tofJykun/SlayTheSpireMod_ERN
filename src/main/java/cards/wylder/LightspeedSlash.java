package cards.wylder;

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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import patches.AbstractCardEnum;

public class LightspeedSlash extends CustomCard {
    public static final String ID = "LightspeedSlash";
    private static final String IMG_PATH = "img/cards/wylder/LightspeedSlash.png";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public LightspeedSlash() {
        super(ID, STRINGS.NAME, IMG_PATH, 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    private int dexterityDamage() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower dexterity = player == null ? null : player.getPower(DexterityPower.POWER_ID);
        return dexterity == null ? 0 : dexterity.amount * this.magicNumber;
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += dexterityDamage();
        try {
            super.applyPowers();
        } finally {
            this.baseDamage = originalBaseDamage;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += dexterityDamage();
        try {
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalBaseDamage;
        }
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightspeedSlash();
    }
}
