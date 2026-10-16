package cards.scholar;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import patches.AbstractCardEnum;
import powers.ElderPrincePower;
import powers.YoungerPrincePower;

public class PowerOfTwinPrinces extends CustomCard {
    public static final String ID = "PowerOfTwinPrinces";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public PowerOfTwinPrinces() {
        super(ID, STRINGS.NAME, "img/cards/scholar/PowerOfTwinPrinces.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        initializeDescription();
    }

    public static boolean usesTestArt() {
        return Settings.PLAYTESTER_ART_MODE
                || (UnlockTracker.betaCardPref != null && UnlockTracker.betaCardPref.getBoolean(ID, false));
    }

    public int getSecondaryAmount() {
        return upgraded ? 2 : 1;
    }

    private int getInitialAmount() {
        return usesTestArt() ? 21 : upgraded ? 4 : 3;
    }

    private String getVariantDescription() {
        return usesTestArt() ? STRINGS.EXTENDED_DESCRIPTION[0] : STRINGS.DESCRIPTION;
    }

    public void refreshVariant() {
        if (baseMagicNumber != getInitialAmount() || !getVariantDescription().equals(rawDescription)) {
            initializeDescription();
        }
    }

    @Override
    public void initializeDescription() {
        baseMagicNumber = magicNumber = getInitialAmount();
        upgradedMagicNumber = upgraded && !usesTestArt();
        rawDescription = getVariantDescription();
        super.initializeDescription();
    }

    @Override
    public void update() {
        refreshVariant();
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        refreshVariant();
        super.render(sb);
    }

    @Override
    public void applyPowers() {
        refreshVariant();
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        refreshVariant();
        // Select the branch now; changing artwork later cannot change queued effects or existing powers.
        if (usesTestArt()) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
            addToBot(new ApplyPowerAction(p, p, new BufferPower(p, getSecondaryAmount()), getSecondaryAmount()));
            addToBot(new ApplyPowerAction(p, p, new ElderPrincePower(p), 1));
        } else {
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
            addToBot(new ApplyPowerAction(p, p, new YoungerPrincePower(p, getSecondaryAmount()), getSecondaryAmount()));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        PowerOfTwinPrinces copy = (PowerOfTwinPrinces) super.makeStatEquivalentCopy();
        copy.initializeDescription();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerOfTwinPrinces();
    }
}
