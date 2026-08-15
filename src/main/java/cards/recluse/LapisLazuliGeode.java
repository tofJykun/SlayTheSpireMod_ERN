package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CrystalCardHelper;
import patches.AbstractCardEnum;

public class LapisLazuliGeode extends CustomCard {
    public static final String ID = "LapisLazuliGeode";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/recluse/LapisLazuliGeode.png";
    private static final int COST = 3;
    private static final int CARD_COUNT = 3;
    private static final int UPGRADE_PLUS_CARD_COUNT = 1;

    public LapisLazuliGeode() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = CARD_COUNT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractCard card = CrystalCardHelper.randomCrystalCard();
            if (card != null) {
                card.setCostForTurn(-99);
                addToBot((AbstractGameAction)new MakeTempCardInHandAction(card, true));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LapisLazuliGeode();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARD_COUNT);
        }
    }
}
