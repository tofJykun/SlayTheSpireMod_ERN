package cards.duchess;

import actions.NoMoneyReturnAction;
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
import patches.AbstractCardEnum;
import powers.MushroomPower;

public class NoMoney extends CustomCard {
    public static final String ID = "NoMoney";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/duchess/NoMoney.png";
    private static final int COST = 0;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public NoMoney() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new MushroomPower((AbstractCreature)p), 1));
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        if (card == null || card.dontTriggerOnUseCard || AbstractDungeon.player == null
                || AbstractDungeon.actionManager == null) {
            return;
        }
        int played = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        AbstractCard returning = general.SmithingBody.physical(this);
        if (played > 0 && this.magicNumber > 0 && played % this.magicNumber == 0
                && (AbstractDungeon.player.drawPile.contains(returning)
                || AbstractDungeon.player.discardPile.contains(returning))) {
            addToBot(new NoMoneyReturnAction(AbstractDungeon.player, returning));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NoMoney();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
