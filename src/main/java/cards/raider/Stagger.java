package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.StaggerPower;
import relics.FighterDestined;

public class Stagger extends CustomCard {
    public static final String ID = "Stagger";
    private static final String IMG_PATH = "img/cards/raider/Stagger.png";
    private static final int COST = 0;
    private static final int ENERGY = 3;
    private static final int DRAW = 3;
    private static final int GREY_HEALTH = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public Stagger() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainEnergyAction(ENERGY));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StaggerPower((AbstractCreature)p, GREY_HEALTH), GREY_HEALTH));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        refreshDescription();
    }

    private void refreshDescription() {
        CardStrings cardStrings = getCardStrings();
        String description = hasFighterDestined(AbstractDungeon.player)
                ? cardStrings.EXTENDED_DESCRIPTION[0]
                : (this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION);
        description = description.replace("!M!", Integer.toString(this.magicNumber));
        description = description.replace("!MS!", Integer.toString(GREY_HEALTH));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    private static boolean hasFighterDestined(AbstractPlayer player) {
        return player != null && player.hasRelic(FighterDestined.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Stagger();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            refreshDescription();
        }
    }
}
