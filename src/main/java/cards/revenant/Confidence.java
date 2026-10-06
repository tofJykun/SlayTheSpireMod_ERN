package cards.revenant;

import basemod.abstracts.CustomCard;
import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.FaithPower;

import java.util.ArrayList;

public class Confidence extends CustomCard {
    public static final String ID = "Confidence";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Confidence() {
        super(ID, STRINGS.NAME, "img/cards/revenant/Confidence.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                isDone = true;
                ArrayList<AbstractCard> hand = new ArrayList<>(p.hand.group);
                for (int i = hand.size() - 1; i >= 0; i--) {
                    AbstractCard card = hand.get(i);
                    if (card instanceof AbstractPhantomCard) {
                        addToTop(new ExhaustSpecificCardAction(card, p.hand));
                    }
                }
            }
        });
        addToBot(new ApplyPowerAction(p, p, new FaithPower(p, magicNumber), magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
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
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Confidence();
    }
}
