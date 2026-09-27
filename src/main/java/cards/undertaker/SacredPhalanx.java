package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SacredPhalanx extends CustomCard {
    public static final String ID = "SacredPhalanx";
    private static final String IMG_PATH = "img/cards/undertaker/SacredPhalanx.png";
    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public SacredPhalanx() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new GainBlockAction(p, p, this.block));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacredPhalanx();
    }
}
