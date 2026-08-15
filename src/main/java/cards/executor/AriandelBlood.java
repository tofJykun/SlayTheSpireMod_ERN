package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.DeathScreen;
import patches.AbstractCardEnum;

public class AriandelBlood extends CustomCard {
    public static final String ID = "AriandelBlood";
    private static final String IMG_PATH = "img/cards/executor/AriandelBlood.png";
    private static final int COST = 0;
    private static final int MAX_HP_LOSS = 1;
    private static final int ENERGY = 2;
    private static final int DRAW = 2;

    public AriandelBlood() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.maxHealth <= MAX_HP_LOSS) {
            p.maxHealth = 0;
            p.currentHealth = 0;
            p.isDead = true;
            p.healthBarUpdatedEvent();
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            return;
        }
        p.decreaseMaxHealth(MAX_HP_LOSS);
        addToBot((AbstractGameAction)new GainEnergyAction(ENERGY));
        addToBot((AbstractGameAction)new DrawCardAction(p, DRAW));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AriandelBlood();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
