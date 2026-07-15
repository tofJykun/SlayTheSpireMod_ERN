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
import powers.SlumberingDragoncrestPower;

public class SlumberingDragoncrest extends CustomCard {
    public static final String ID = "SlumberingDragoncrest";
    private static final String IMG_PATH = "img/cards/recluse/SlumberingDragoncrest.png";
    private static final int COST = 1;
    private static final int WEAK = 1;

    public SlumberingDragoncrest() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.POWER,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = WEAK;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded && p.hasPower(SlumberingDragoncrestPower.POWER_ID)) {
            ((SlumberingDragoncrestPower)p.getPower(SlumberingDragoncrestPower.POWER_ID)).upgradeToAllEnemies();
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SlumberingDragoncrestPower((AbstractCreature)p, this.magicNumber, this.upgraded),
                this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SlumberingDragoncrest();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
