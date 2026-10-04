package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;

public class SunlightShield extends CustomCard {
    public static final String ID = "SunlightShield";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SunlightShield() {
        super(ID, STRINGS.NAME, "img/cards/guardian/SunlightShield.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                AbstractPower vigor = p.getPower(VigorPower.POWER_ID);
                if (vigor != null && vigor.amount > 0) {
                    addToTop(new ApplyPowerAction(p, p, new VigorPower(p, vigor.amount), vigor.amount));
                }
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(4);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SunlightShield();
    }
}
