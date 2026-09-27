package cards.undertaker;

import actions.ClockwiseSequencer;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SpiralhornShield extends CustomCard {
    public static final String ID = "SpiralhornShield";
    private static final String IMG_PATH = "img/cards/undertaker/SpiralhornShield.png";
    private static final int COST = 2;
    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 6;
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SpiralhornShield() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.BASIC, CardTarget.SELF);
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        ClockwiseSequencer.requestRight(1);
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpiralhornShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
