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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;

public class CourtesyEnough extends CustomCard {
    public static final String ID = "CourtesyEnough";
    private static final String IMG_PATH = "img/cards/raider/CourtesyEnough.png";
    private static final int COST = -2;
    private static final int STAT_GAIN = 3;
    private static final int UPGRADE_PLUS_STAT_GAIN = 1;

    public CourtesyEnough() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = STAT_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerOnExhaust() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            return;
        }
        AbstractGameAction strength = new ApplyPowerAction((AbstractCreature)player, (AbstractCreature)player,
                new StrengthPower((AbstractCreature)player, this.magicNumber), this.magicNumber);
        AbstractGameAction dexterity = new ApplyPowerAction((AbstractCreature)player, (AbstractCreature)player,
                new DexterityPower((AbstractCreature)player, this.magicNumber), this.magicNumber);
        addToTop(dexterity);
        addToTop(strength);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = getCardStrings().EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new CourtesyEnough();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STAT_GAIN);
        }
    }
}
