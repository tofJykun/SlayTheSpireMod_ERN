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
import patches.AbstractCardEnum;
import powers.LockoutPower;

public class GiantDoorShield extends CustomCard {
    public static final String ID = "GiantDoorShield";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public GiantDoorShield() {
        super(ID, STRINGS.NAME, "img/cards/guardian/GiantDoorShield.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                if (p.hasPower(LockoutPower.POWER_ID)) {
                    addToTop(new RemoveSpecificPowerAction(p, p, LockoutPower.POWER_ID));
                } else {
                    addToTop(new ApplyPowerAction(p, p, new LockoutPower(p), 1));
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
        return new GiantDoorShield();
    }
}
