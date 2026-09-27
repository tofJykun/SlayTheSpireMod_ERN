package cards.raider;

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
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;
import relics.FighterDestined;

public class BloodWall extends CustomCard {
    public static final String ID = "BloodWall";
    private static final String IMG_PATH = "img/cards/raider/BloodWall.png";
    private static final int COST = 2;
    private static final int GREY_HEALTH = 2;
    private static final int BLOCK_AMT = 18;
    private static final int UPGRADE_PLUS_BLOCK = 6;

    public BloodWall() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_AMT;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (hasFighterDestined(p)) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPlusPower((AbstractCreature)p, GREY_HEALTH), GREY_HEALTH));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPower((AbstractCreature)p, GREY_HEALTH), GREY_HEALTH));
        }
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
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
                : cardStrings.DESCRIPTION;
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
        return new BloodWall();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
