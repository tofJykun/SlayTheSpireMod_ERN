package cards.revenant;

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
import powers.PerfectScorePower;

public class PerfectScore extends CustomCard {
    public static final String ID = "PerfectScore";
    private static final String IMG_PATH = "img/cards/revenant/PerfectScore.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public PerfectScore() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new PerfectScorePower((AbstractCreature)p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PerfectScore();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
