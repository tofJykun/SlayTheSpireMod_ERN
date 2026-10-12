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
import patches.FragileField;
import potions.ElixirOfLife;

public class PenglaiWorship extends CustomCard {
    public static final String ID = "PenglaiWorship";
    private static final String IMG_PATH = "img/cards/scholar/PenglaiWorship.png";
    private static final int COST = 1;
    private static final int GOLD_LOSS = 5;

    public PenglaiWorship() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(CardTags.HEALING);
        FragileField.setBase(this, 0);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot((AbstractGameAction)new ObtainPotionAction(new ElixirOfLife()));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        AbstractDungeon.player.loseGold(getGoldLoss());
    }

    public int getGoldLoss() {
        return GOLD_LOSS;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PenglaiWorship();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
