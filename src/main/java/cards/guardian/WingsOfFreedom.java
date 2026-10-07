package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.HornetRingPower;
import powers.HoverPower;

public class WingsOfFreedom extends CustomCard {
    public static final String ID = "WingsOfFreedom";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public WingsOfFreedom() {
        super(ID, STRINGS.NAME, "img/cards/guardian/WingsOfFreedom.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public int getHoverPerEnemy() {
        return 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hover = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead
                    && HornetRingPower.isAttackIntent(monster.intent)) {
                hover += getHoverPerEnemy();
            }
        }
        addToBot(new DrawCardAction(p, this.magicNumber));
        if (hover > 0) {
            addToBot(new ApplyPowerAction(p, p, new HoverPower(p, hover), hover));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WingsOfFreedom();
    }
}
