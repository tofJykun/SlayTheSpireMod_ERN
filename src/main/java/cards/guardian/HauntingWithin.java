package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PostureBreakPower;

public class HauntingWithin extends CustomCard {
    public static final String ID = "HauntingWithin";
    private static final String IMG_PATH = "img/cards/guardian/HauntingWithin.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public HauntingWithin() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PostureBreakPower(m), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HauntingWithin();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
