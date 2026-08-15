package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SoulVesselPower;

public class SoulVessel extends CustomCard {
    public static final String ID = "SoulVessel";
    private static final String IMG_PATH = "img/cards/recluse/SoulVessel.png";
    private static final int COST = 1;
    private static final int BLOCK_PER_POWER = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public SoulVessel() {
        this(0);
    }

    public SoulVessel(int upgrades) {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.POWER,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BLOCK_PER_POWER;
        this.magicNumber = this.baseMagicNumber;
        for (int i = 0; i < upgrades; i++) {
            applyUpgrade();
        }
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SoulVesselPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SoulVessel(this.timesUpgraded);
    }

    @Override
    public void upgrade() {
        applyUpgrade();
    }

    private void applyUpgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = getCardStrings().NAME + "+" + this.timesUpgraded;
        initializeTitle();
        initializeDescription();
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }
}
