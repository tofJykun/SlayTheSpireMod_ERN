package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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

public class PierceShield extends CustomCard {
    public static final String ID = "PierceShield";
    private static final String IMG_PATH = "img/cards/raider/PierceShield.png";
    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public PierceShield() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        updateDrawAmount();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, hasGreyHealth(p) ? DRAW + 1 : DRAW));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDrawAmount();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updateDrawAmount();
    }

    @Override
    public void triggerOnGlowCheck() {
        updateDrawAmount();
    }

    private void updateDrawAmount() {
        this.baseMagicNumber = hasGreyHealth(AbstractDungeon.player) ? DRAW + 1 : DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    private static boolean hasGreyHealth(AbstractPlayer player) {
        return player != null && (player.hasPower(GreyHealthPower.POWER_ID)
                || player.hasPower(GreyHealthPlusPower.POWER_ID));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PierceShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
