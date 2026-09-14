package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.LoseStrengthAtEndOfTurnPower;

public class RiteOfKindling extends CustomCard {
    public static final String ID = "RiteOfKindling";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/scholar/RiteOfKindling.png";
    private static final int COST = 0;
    private static final int ENERGY = 2;
    private static final int UPGRADED_ENERGY = 3;
    private static final int STRENGTH_LOSS = 2;

    public RiteOfKindling() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = ENERGY;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainEnergyAction(this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new LoseStrengthAtEndOfTurnPower((AbstractCreature)p, STRENGTH_LOSS), STRENGTH_LOSS));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (!hasNonNegativeStrength(p)) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = hasNonNegativeStrength(AbstractDungeon.player)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private boolean hasNonNegativeStrength(AbstractPlayer p) {
        return p != null
                && (!p.hasPower(StrengthPower.POWER_ID) || p.getPower(StrengthPower.POWER_ID).amount >= 0);
    }

    @Override
    public AbstractCard makeCopy() {
        return new RiteOfKindling();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADED_ENERGY;
            this.magicNumber = this.baseMagicNumber;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
