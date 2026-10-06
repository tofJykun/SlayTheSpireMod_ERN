package cards.tempcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.AbstractSummonPower;
import powers.AsimiEchoPower;
import powers.SummonAsimi;
import summons.SummonAnimationManager;

public class PhantomAsimi extends AbstractPhantomCard {
    public static final String ID = "PhantomAsimi";
    private static final String IMG_PATH = "img/cards/tempcards/PhantomAsimi.png";
    private static final int COST = 1;

    public PhantomAsimi() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
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
        if (!AbstractSummonPower.isActiveSummon((AbstractCreature)p, SummonAsimi.POWER_ID)) {
            this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null
                && AbstractSummonPower.isActiveSummon((AbstractCreature)AbstractDungeon.player, SummonAsimi.POWER_ID)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        SummonAnimationManager.triggerAttack(SummonAsimi.SUMMON_KEY);
        if (!p.hasPower(AsimiEchoPower.POWER_ID)) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new AsimiEchoPower((AbstractCreature)p), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new PhantomAsimi();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
