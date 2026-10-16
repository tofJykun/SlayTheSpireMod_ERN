package cards.scholar;

import actions.OriginalSinAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;

public class OriginalSin extends CustomCard {
    public static final String ID = "OriginalSin";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public OriginalSin() {
        super(ID, STRINGS.NAME, "img/cards/scholar/OriginalSin.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        FragileField.setBase(this, 7);
        initializeDescription();
    }

    public int getDrawAmount() { return this.upgraded ? 3 : 2; }

    @Override
    public void initializeDescription() {
        int stage = FragileField.isFragile(this) ? FragileField.value(this) : 7;
        stage = Math.max(0, Math.min(7, stage));
        this.baseMagicNumber = this.magicNumber = stage >= 6 ? 6 : stage == 3 ? 20 : stage == 2 ? 5 : stage == 0 ? 777 : 0;
        this.rawDescription = stage == 7 ? STRINGS.DESCRIPTION : STRINGS.EXTENDED_DESCRIPTION[6 - stage];
        super.initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // UseCardAction decrements Fragile after use(); capture the old stage now.
        addToBot(new OriginalSinAction(p, FragileField.state(this).remaining, getDrawAmount()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override public AbstractCard makeCopy() { return new OriginalSin(); }
}
