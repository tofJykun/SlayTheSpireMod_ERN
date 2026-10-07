package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import patches.AbstractCardEnum;
import powers.PerfectGuardPower;

public class DragoncrestGreatshield extends CustomCard {
    public static final String ID = "DragoncrestGreatshield";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int PERFECT_GUARD = 6;

    public DragoncrestGreatshield() {
        super(ID, STRINGS.NAME, "img/cards/guardian/DragoncrestGreatshield.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 22;
    }

    public int getPerfectGuardAmount() {
        return upgraded ? 10 : PERFECT_GUARD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new PerfectGuardPower(p, getPerfectGuardAmount()), getPerfectGuardAmount()));
        addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(8);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DragoncrestGreatshield();
    }
}
