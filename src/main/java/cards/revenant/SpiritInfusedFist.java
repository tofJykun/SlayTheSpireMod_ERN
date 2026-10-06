package cards.revenant;

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
import powers.SpiritPower;

public class SpiritInfusedFist extends CustomCard {
    public static final String ID = "SpiritInfusedFist";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SpiritInfusedFist() {
        super(ID, STRINGS.NAME, "img/cards/revenant/SpiritInfusedFist.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 7;
    }

    private int spiritDamage() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower spirit = player == null ? null : player.getPower(SpiritPower.POWER_ID);
        return spirit == null ? 0 : spirit.amount * magicNumber;
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = baseDamage;
        baseDamage += spiritDamage();
        try {
            super.applyPowers();
        } finally {
            baseDamage = originalBaseDamage;
        }
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = baseDamage;
        baseDamage += spiritDamage();
        try {
            super.calculateCardDamage(monster);
        } finally {
            baseDamage = originalBaseDamage;
        }
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpiritInfusedFist();
    }
}
