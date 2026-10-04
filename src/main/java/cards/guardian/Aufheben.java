package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;
import powers.HoverPower;

public class Aufheben extends CustomCard {
    public static final String ID = "Aufheben";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Aufheben() {
        super(ID, STRINGS.NAME, "img/cards/guardian/Aufheben.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower vigor = p.getPower(VigorPower.POWER_ID);
                AbstractPower hover = p.getPower(HoverPower.POWER_ID);
                int strength = vigor == null ? 0 : Math.max(0, vigor.amount);
                int dexterity = hover == null ? 0 : Math.max(0, hover.amount);
                // Queue in reverse so both removals finish before either attribute is gained.
                if (dexterity > 0) {
                    addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, dexterity), dexterity));
                }
                if (strength > 0) {
                    addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, strength), strength));
                }
                if (hover != null) {
                    addToTop(new RemoveSpecificPowerAction(p, p, HoverPower.POWER_ID));
                }
                if (vigor != null) {
                    addToTop(new RemoveSpecificPowerAction(p, p, VigorPower.POWER_ID));
                }
                isDone = true;
            }
        });
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
        return new Aufheben();
    }
}
