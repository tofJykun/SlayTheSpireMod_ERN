package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingStats;
import patches.AbstractCardEnum;

public class SmithingArtSpears extends CustomCard {
    public static final String ID = "SmithingArtSpears";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SmithingArtSpears() {
        super(ID, STRINGS.NAME, "img/cards/raider/SmithingArtSpears.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 12;
        refreshSmithingCount();
    }

    public int getDamagePerSmithing() {
        return upgraded ? 6 : 4;
    }

    private void refreshSmithingCount() {
        baseMagicNumber = magicNumber = SmithingStats.getCombatSmithings();
        isMagicNumberModified = false;
    }

    @Override
    public void applyPowers() {
        refreshSmithingCount();
        int originalBaseDamage = baseDamage;
        baseDamage += magicNumber * getDamagePerSmithing();
        super.applyPowers();
        baseDamage = originalBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        refreshSmithingCount();
        int originalBaseDamage = baseDamage;
        baseDamage += magicNumber * getDamagePerSmithing();
        super.calculateCardDamage(monster);
        baseDamage = originalBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        calculateCardDamage(monster);
        addToBot(new DamageAction(monster, new DamageInfo(player, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SmithingArtSpears();
    }
}
