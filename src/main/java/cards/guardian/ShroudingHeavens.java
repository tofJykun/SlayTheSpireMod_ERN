package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.DeepSpacePower;
import powers.EternalSleepPower;

public class ShroudingHeavens extends CustomCard {
    public static final String ID = "ShroudingHeavens";

    public ShroudingHeavens() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/guardian/ShroudingHeavens.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ENEMY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(DeepSpacePower.POWER_ID) && m != null
                && !m.isDeadOrEscaped() && !m.isDying && !m.halfDead) {
            addToBot(new ApplyPowerAction(m, p, new EternalSleepPower(m), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShroudingHeavens();
    }
}
