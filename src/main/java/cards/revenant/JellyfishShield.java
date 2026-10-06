package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.JellyfishShieldPower;

public class JellyfishShield extends CustomCard {
    public static final String ID = "JellyfishShield";
    private static final String IMG_PATH = "img/cards/revenant/JellyfishShield.png";

    public JellyfishShield() {
        super(ID, strings().NAME, IMG_PATH, -1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 5;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int x = Math.max(0, this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse);
        if (p.hasRelic("Chemical X")) {
            x += 2;
            p.getRelic("Chemical X").flash();
        }
        for (int i = 0; i < x; i++) {
            addToBot(new GainBlockAction(p, p, this.block));
        }
        if (x > 0) {
            int bonus = 50 * x;
            addToBot(new ApplyPowerAction(p, p, new JellyfishShieldPower(p, bonus), bonus));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new JellyfishShield();
    }
}
