package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class ExpectAFight extends CustomCard {
    public static final String ID = "ExpectAFight";
    private static final String IMG_PATH = "img/cards/raider/ExpectAFight.png";
    private static final int COST = 1;

    public ExpectAFight() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int attacks = countAttacksInHand(p);
        this.baseMagicNumber = attacks;
        this.magicNumber = attacks;
        addToBot((AbstractGameAction)new GainEnergyAction(attacks));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateMagicNumber();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updateMagicNumber();
    }

    @Override
    public void triggerOnGlowCheck() {
        updateMagicNumber();
    }

    private void updateMagicNumber() {
        int attacks = countAttacksInHand(AbstractDungeon.player);
        this.baseMagicNumber = attacks;
        this.magicNumber = attacks;
        this.isMagicNumberModified = false;
    }

    private static int countAttacksInHand(AbstractPlayer player) {
        if (player == null || player.hand == null) {
            return 0;
        }

        int count = 0;
        for (AbstractCard card : player.hand.group) {
            if (card.type == CardType.ATTACK) {
                count++;
            }
        }
        return count;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ExpectAFight();
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
