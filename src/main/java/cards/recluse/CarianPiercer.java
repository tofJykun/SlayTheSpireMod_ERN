package cards.recluse;

import basemod.abstracts.CustomCard;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ParryPower;

public class CarianPiercer extends CustomCard {
    public static final String ID = "CarianPiercer";
    private static final String IMG_PATH = "img/cards/recluse/CarianPiercer.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int PARRY = 2;

    public CarianPiercer() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new FadingPrimalGlintstone();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(new FadingPrimalGlintstone(), 1));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new ParryPower((AbstractCreature)p, PARRY), PARRY));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new CarianPiercer();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
