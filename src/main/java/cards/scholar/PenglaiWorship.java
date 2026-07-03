package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import potions.ElixirOfLife;

public class PenglaiWorship extends CustomCard {
    public static final String ID = "PenglaiWorship";
    private static final String IMG_PATH = "img/cards/scholar/Defend.png";
    private static final int COST = 1;
    private static final int GOLD_LOSS = 20;
    private static final int UPGRADE_GOLD_LOSS = -10;

    public PenglaiWorship() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = GOLD_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ObtainPotionAction(new ElixirOfLife()));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        AbstractDungeon.player.loseGold(this.magicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new PenglaiWorship();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_GOLD_LOSS);
            initializeDescription();
        }
    }
}
