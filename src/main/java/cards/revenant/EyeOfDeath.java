package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;
import powers.DeathblightPower;
import powers.GraftedDragonPower;

public class EyeOfDeath extends CustomCard {
    public static final String ID = "EyeOfDeath";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public EyeOfDeath() {
        super(ID, STRINGS.NAME, "img/cards/revenant/EyeOfDeath.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
        refreshDescription();
    }

    public int getSummonLockTurns() {
        return upgraded ? 1 : 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new ApplyPowerAction(m, p, new DeathblightPower(m, magicNumber), magicNumber));
        }
        AbstractSummonPower summon = AbstractSummonPower.getActiveSummon(p);
        if (summon != null) {
            addToBot(new RemoveSpecificPowerAction(p, p, summon));
        }
        int turns = getSummonLockTurns();
        addToBot(new ApplyPowerAction(p, p, new GraftedDragonPower(p, turns), turns));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (!AbstractSummonPower.hasSummonPower(p)) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractDungeon.player != null && AbstractSummonPower.hasSummonPower(AbstractDungeon.player)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    private void refreshDescription() {
        String description = STRINGS.DESCRIPTION.replace("!MS!", Integer.toString(getSummonLockTurns()));
        if (!description.equals(rawDescription)) {
            rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            refreshDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EyeOfDeath();
    }
}
