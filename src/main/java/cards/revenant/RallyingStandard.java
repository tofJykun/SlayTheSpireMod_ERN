package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;
import powers.RallyingStandardPower;

public class RallyingStandard extends CustomCard {
    public static final String ID = "RallyingStandard";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public RallyingStandard() {
        super(ID, STRINGS.NAME, "img/cards/revenant/RallyingStandard.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractSummonPower summon = AbstractSummonPower.getActiveSummon(p);
        if (summon != null) {
            addToBot(new ApplyPowerAction(p, p, new RallyingStandardPower(p, summon), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RallyingStandard();
    }
}
