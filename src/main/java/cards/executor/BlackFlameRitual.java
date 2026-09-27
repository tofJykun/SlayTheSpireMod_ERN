package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BlackFlameRitualPower;

public class BlackFlameRitual extends CustomCard {
    public static final String ID = "BlackFlameRitual";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/executor/BlackFlameRitual.png";
    private static final int COST = 3;

    public BlackFlameRitual() {
        super(ID, STRINGS.NAME, IMG_PATH, COST, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new BlackFlameRitualPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackFlameRitual();
    }
}
