package cards.ironeye;

import actions.LoseAllVirtualCurrencyAction;
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
import patches.AbstractCardEnum;
import powers.VirtualCurrencyPower;

public class GoldenCrux extends CustomCard {
    public static final String ID = "GoldenCrux";
    private static final String IMG_PATH = "img/cards/ironeye/GoldenCrux.png";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public GoldenCrux() {
        super(ID, STRINGS.NAME, IMG_PATH, 2, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 2;
        this.exhaust = true;
    }

    private int tokenDamage() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower tokens = player == null ? null : player.getPower(VirtualCurrencyPower.POWER_ID);
        return tokens == null ? 0 : Math.max(0, tokens.amount) * this.magicNumber;
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;
        this.baseDamage += tokenDamage();
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
        this.baseDamage += tokenDamage();
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
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new LoseAllVirtualCurrencyAction(player));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GoldenCrux();
    }
}
