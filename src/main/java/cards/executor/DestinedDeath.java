package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.DeathblightPower;

public class DestinedDeath extends CustomCard {
    public static final String ID = "DestinedDeath";
    private static final String IMG_PATH = "img/cards/executor/DestinedDeath.png";
    private static final int COST = 2;
    private static final int DEATHBLIGHT = 1;
    private static final int SELF_DEATHBLIGHT = 2;
    private static final int UPGRADE_PLUS_DEATHBLIGHT = 1;

    public DestinedDeath() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = DEATHBLIGHT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new DeathblightPower((AbstractCreature)m, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player,
                (AbstractCreature)AbstractDungeon.player,
                new DeathblightPower((AbstractCreature)AbstractDungeon.player, SELF_DEATHBLIGHT),
                SELF_DEATHBLIGHT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DestinedDeath();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEATHBLIGHT);
            initializeDescription();
        }
    }
}
