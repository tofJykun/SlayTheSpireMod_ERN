package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.HoverPower;

public class FatalAppetite extends CustomCard {
    public static final String ID = "FatalAppetite";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public FatalAppetite() {
        super(ID, STRINGS.NAME, "img/cards/guardian/FatalAppetite.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    public int getMaxHpGain() {
        return upgraded ? 10 : 7;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        final int cap = getMaxHpGain();
        final int gainPerStack = magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower hover = player.getPower(HoverPower.POWER_ID);
                if (hover != null) {
                    final int gain = Math.min(cap, Math.max(0, hover.amount) * gainPerStack);
                    if (gain > 0) {
                        addToTop(new AbstractGameAction() {
                            @Override
                            public void update() {
                                player.increaseMaxHp(gain, true);
                                isDone = true;
                            }
                        });
                    }
                    // Finish removal and its flight-state hooks before granting max HP.
                    addToTop(new RemoveSpecificPowerAction(player, player, HoverPower.POWER_ID));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FatalAppetite();
    }
}
