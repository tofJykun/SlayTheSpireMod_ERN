package cards.wylder;

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
import powers.ParryPower;

public class BlueWhiteWoodenShield extends CustomCard {
    public static final String ID = "BlueWhiteWoodenShield";
    private static final String IMG_PATH = "img/cards/wylder/BlueWhiteWoodenShield.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int PARRY = 5;

    public BlueWhiteWoodenShield() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 0;
        this.baseMagicNumber = PARRY;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new ParryPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.baseBlock = AbstractDungeon.player.drawPile.size();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseBlock = AbstractDungeon.player.drawPile.size();
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlueWhiteWoodenShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
