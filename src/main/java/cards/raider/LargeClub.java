package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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

public class LargeClub extends CustomCard {
    public static final String ID = "LargeClub";
    private static final String IMG_PATH = "img/cards/raider/LargeClub.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int GREY_HEALTH = 1;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public LargeClub() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (hasFighterDestined(p)) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPlusPower((AbstractCreature)p, GREY_HEALTH), GREY_HEALTH));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPower((AbstractCreature)p, GREY_HEALTH), GREY_HEALTH));
        }
        addToBot((AbstractGameAction)new DrawCardAction(this.magicNumber));
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
                ? (this.upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0])
                : (this.upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION);
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    private static boolean hasFighterDestined(AbstractPlayer player) {
        return player != null && player.hasRelic(FighterDestined.ID);
    }

    public int getGreyHealth() {
        return GREY_HEALTH;
    }

    public int getBaseGreyHealth() {
        return GREY_HEALTH;
    }

    @Override
    public AbstractCard makeCopy() {
        return new LargeClub();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            refreshDescription();
        }
    }
}
