package cards.recluse;

import actions.ArchiveOccultAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import cards.status.MagicEmber;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class ArchiveOccult extends CustomCard {
    public static final String ID = "ArchiveOccult";
    private static final String IMG_PATH = "img/cards/recluse/ArchiveOccult.png";
    private static final int COST = 2;
    private static final int BLOCK = 20;
    private static final int UPGRADE_PLUS_BLOCK = 10;

    public ArchiveOccult() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.exhaust = true;
        MultiCardPreview.add(this, true, new FadingPrimalGlintstone(), new MagicEmber());
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction(p, p, this.block));
        addToBot((AbstractGameAction)new ArchiveOccultAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArchiveOccult();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
