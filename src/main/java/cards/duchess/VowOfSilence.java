package cards.duchess;

import cards.AbstractScheduledCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;

public class VowOfSilence extends AbstractScheduledCard {
    public static final String ID = "VowOfSilence";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public VowOfSilence() {
        super(ID, STRINGS.NAME, "img/cards/duchess/VowOfSilence.png", 3, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY, 4);
        this.baseBlock = 16;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new ApplyPowerAction(monster, p,
                        new WeakPower(monster, this.magicNumber, false), this.magicNumber));
            }
        }
        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(4);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VowOfSilence();
    }
}
