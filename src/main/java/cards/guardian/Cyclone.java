package cards.guardian;

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
import powers.HoverPower;
import powers.SoarPower;

public class Cyclone extends CustomCard {
    public static final String ID = "Cyclone";
    private static final String IMG_PATH = "img/cards/guardian/Cyclone.png";
    private static final int COST = 1;

    public Cyclone() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        multiplyPower(p, HoverPower.POWER_ID, this.upgraded ? 3 : 2);
        multiplyPower(p, SoarPower.POWER_ID, this.upgraded ? 3 : 2);
    }

    private void multiplyPower(AbstractPlayer p, String powerId, int multiplier) {
        if (p.hasPower(powerId)) {
            int amount = p.getPower(powerId).amount;
            int gain = amount * (multiplier - 1);
            if (gain > 0) {
                if (HoverPower.POWER_ID.equals(powerId)) {
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                            new HoverPower((AbstractCreature)p, gain), gain));
                } else if (SoarPower.POWER_ID.equals(powerId)) {
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                            new SoarPower((AbstractCreature)p, gain), gain));
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cyclone();
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
