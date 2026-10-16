package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class BipolarDisorder extends CustomCard {
    public static final String ID = "BipolarDisorder";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private boolean wrath;

    public BipolarDisorder() {
        super(ID, STRINGS.NAME, "img/cards/undertaker/BipolarDisorder.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 7;
    }

    private void refreshDescription() {
        rawDescription = wrath ? STRINGS.EXTENDED_DESCRIPTION[upgraded ? 1 : 0]
                : (upgraded ? STRINGS.UPGRADE_DESCRIPTION : STRINGS.DESCRIPTION);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ChangeStanceAction(wrath ? "Wrath" : "Calm"));
        wrath = !wrath;
        refreshDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            refreshDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BipolarDisorder();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        BipolarDisorder copy = (BipolarDisorder)super.makeStatEquivalentCopy();
        copy.wrath = wrath;
        copy.refreshDescription();
        return copy;
    }
}
