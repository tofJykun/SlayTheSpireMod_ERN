package cards.recluse;

import actions.TheGnawlingAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.IntelligencePower;

public class TheGnawling extends CustomCard {
    public static final String ID = "TheGnawling";
    private static final String IMG_PATH = "img/cards/recluse/TheGnawling.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 8;
    private static final int BASE_INTELLIGENCE = 1;

    public TheGnawling() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = BASE_INTELLIGENCE;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int intelligenceGain = BASE_INTELLIGENCE + this.misc;
        addToBot((AbstractGameAction)new TheGnawlingAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn), this.uuid));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new IntelligencePower((AbstractCreature)p, intelligenceGain), intelligenceGain));
    }

    @Override
    public void applyPowers() {
        syncPermanentIntelligence();
        super.applyPowers();
        resetPermanentIntelligenceDisplay();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        syncPermanentIntelligence();
        super.calculateCardDamage(mo);
        resetPermanentIntelligenceDisplay();
    }

    private void syncPermanentIntelligence() {
        this.baseMagicNumber = BASE_INTELLIGENCE + this.misc;
        this.magicNumber = this.baseMagicNumber;
    }

    private void resetPermanentIntelligenceDisplay() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    public void refreshPermanentIntelligenceDisplay() {
        syncPermanentIntelligence();
        resetPermanentIntelligenceDisplay();
        resetDamageDisplay();
        initializeDescription();
    }

    private void resetDamageDisplay() {
        this.damage = this.baseDamage;
        this.isDamageModified = false;
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        syncPermanentIntelligence();
        resetPermanentIntelligenceDisplay();
    }

    @Override
    public void onMoveToDiscard() {
        resetDamageDisplay();
        resetPermanentIntelligenceDisplay();
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheGnawling();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
