package cards.executor;

import basemod.abstracts.CustomCard;
import cards.tempcards.CrucibleToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.CrucibleCodexPower;

public class CrucibleCodex extends CustomCard {
    public static final String ID = "CrucibleCodex";
    private static final String IMG_PATH = "img/cards/executor/CrucibleCodex.png";
    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;

    public CrucibleCodex() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.cardsToPreview = new CrucibleToken();
        this.isEthereal = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new CrucibleCodexPower((AbstractCreature)p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrucibleCodex();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
