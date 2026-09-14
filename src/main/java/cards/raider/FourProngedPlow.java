package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class FourProngedPlow extends CustomCard {
    public static final String ID = "FourProngedPlow";
    private static final String IMG_PATH = "img/cards/raider/FourProngedPlow.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 9;
    private static final int GREY_HEALTH = 1;
    private static final int GREY_HEALTH_PLUS = 1;
    private static final int DRAW = 3;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public FourProngedPlow() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        int greyHealth = hasFighterDestined(p) ? GREY_HEALTH_PLUS : GREY_HEALTH;
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (hasFighterDestined(p)) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPlusPower((AbstractCreature)p, greyHealth), greyHealth));
        } else {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GreyHealthPower((AbstractCreature)p, greyHealth), greyHealth));
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
        return hasFighterDestined(AbstractDungeon.player) ? GREY_HEALTH_PLUS : GREY_HEALTH;
    }

    public int getBaseGreyHealth() {
        return GREY_HEALTH;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FourProngedPlow();
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
