package cards.tempcards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.AbstractSummonPower;
import powers.SummonSebastian;
import summons.SummonAnimationManager;

public class PhantomSebastian extends AbstractPhantomCard {
    public static final String ID = "PhantomSebastian";
    private static final String IMG_PATH = "img/cards/tempcards/PhantomSebastian.png";
    private static final int COST = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public PhantomSebastian() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.isEthereal = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (!AbstractSummonPower.isActiveSummon((AbstractCreature)p, SummonSebastian.POWER_ID)) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null
                && AbstractSummonPower.isActiveSummon((AbstractCreature)AbstractDungeon.player, SummonSebastian.POWER_ID)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SummonAnimationManager.triggerAttack(SummonSebastian.SUMMON_KEY);
        addToBot(new GainEnergyAction(this.upgraded ? 3 : 2));
        addToBot(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new PhantomSebastian();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
