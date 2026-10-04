package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;

public class CelebrantCleaver extends CustomCard {
    public static final String ID = "CelebrantCleaver";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CelebrantCleaver() {
        super(ID, STRINGS.NAME, "img/cards/revenant/CelebrantCleaver.png", 1,
                STRINGS.DESCRIPTION, CardType.ATTACK, AbstractCardEnum.Revenant_COLOR,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 16;
        isMultiDamage = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (!AbstractSummonPower.hasSummonPower(p)) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = (AbstractSummonPower.hasSummonPower(AbstractDungeon.player)
                ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR).cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        AbstractSummonPower summon = AbstractSummonPower.getActiveSummon(p);
        if (summon != null) {
            addToBot(new RemoveSpecificPowerAction(p, p, summon));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CelebrantCleaver();
    }
}
