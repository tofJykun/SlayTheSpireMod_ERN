package cards.raider;

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
import powers.MasterworkPower;
import relics.FighterDestined;

public class Masterwork extends CustomCard {
    public static final String ID = "Masterwork";
    private static final String IMG_PATH = "img/cards/raider/Masterwork.png";
    private static final int COST = 2;
    private static final int BASE_LOSS = 8;
    private static final int UPGRADE_LOSS = 6;

    public Masterwork() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BASE_LOSS;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new MasterworkPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
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

    @Override
    public AbstractCard makeCopy() {
        return new Masterwork();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_LOSS);
            refreshDescription();
            initializeDescription();
        }
    }
}
