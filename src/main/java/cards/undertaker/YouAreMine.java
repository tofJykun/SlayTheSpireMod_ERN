package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;

public class YouAreMine extends CustomCard {
    public static final String ID = "YouAreMine";
    private static final String IMG_PATH = "img/cards/undertaker/YouAreMine.png";
    private static final int COST = 3;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 10;
    private static final int DEBUFF = 7;

    public YouAreMine() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = DEBUFF;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new WeakPower((AbstractCreature)monster, this.magicNumber, false), this.magicNumber));
                addToBot(new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new VulnerablePower((AbstractCreature)monster, this.magicNumber, false), this.magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new YouAreMine();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
