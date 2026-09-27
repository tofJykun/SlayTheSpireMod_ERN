package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;

public class Gravitas extends CustomCard {
    public static final String ID = "Gravitas";
    private static final String IMG_PATH = "img/cards/undertaker/Gravitas.png";
    private static final int COST = 2;
    private static final int BLOCK = 14;
    private static final int UPGRADE_PLUS_BLOCK = 6;
    private static final int POISE_BREAK = 2;

    public Gravitas() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = POISE_BREAK;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p,
                        new PoiseBreakPower(monster, this.magicNumber), this.magicNumber));
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
        return new Gravitas();
    }
}
