package cards.recluse;

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
import powers.HiddenBodyPower;

public class HiddenBody extends CustomCard {
    public static final String ID = "HiddenBody";
    private static final String IMG_PATH = "img/cards/recluse/HiddenBody.png";
    private static final int COST = 1;
    private static final int COST_UPGRADE = 0;
    private static final int WEAK = 2;

    public HiddenBody() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new HiddenBodyPower((AbstractCreature)p, WEAK), WEAK));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new HiddenBody();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(COST_UPGRADE);
        }
    }
}
