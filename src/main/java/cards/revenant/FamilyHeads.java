package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.FaithPower;
import powers.SpiritPower;

public class FamilyHeads extends CustomCard {
    public static final String ID = "FamilyHeads";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public FamilyHeads() {
        super(ID, STRINGS.NAME, "img/cards/revenant/FamilyHeads.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        convert(p, true, magicNumber);
        convert(p, false, magicNumber);
    }

    private void convert(AbstractPlayer player, boolean spiritToFaith, int divisor) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower source = player.getPower(spiritToFaith ? SpiritPower.POWER_ID : FaithPower.POWER_ID);
                int gain = source == null ? 0 : Math.max(0, source.amount) / Math.max(1, divisor);
                if (gain > 0) {
                    AbstractPower result = spiritToFaith ? new FaithPower(player, gain) : new SpiritPower(player, gain);
                    // Finish applying the first gain before the second conversion reads its source.
                    addToTop(new ApplyPowerAction(player, player, result, gain));
                }
                isDone = true;
            }
        });
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (EnergyPanel.totalCount != 0) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = (EnergyPanel.totalCount == 0 ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR).cpy();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(-1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FamilyHeads();
    }
}
