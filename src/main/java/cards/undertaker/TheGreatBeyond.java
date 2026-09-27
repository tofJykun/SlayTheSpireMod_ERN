package cards.undertaker;

import actions.PlayRandomHandCardAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class TheGreatBeyond extends CustomCard {
    public static final String ID = "TheGreatBeyond";
    private static final String IMG_PATH = "img/cards/undertaker/TheGreatBeyond.png";
    private static final int COST = 3;
    private static final int PLAY_COUNT = 6;
    private static final int UPGRADE_COST = 2;

    public TheGreatBeyond() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = PLAY_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot((AbstractGameAction)new PlayRandomHandCardAction());
        }
        addToBot((AbstractGameAction)new PressEndTurnButtonAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheGreatBeyond();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
