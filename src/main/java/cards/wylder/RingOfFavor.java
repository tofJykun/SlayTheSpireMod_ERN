package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.RingOfFavorPower;

public class RingOfFavor extends CustomCard {
    public static final String ID = "RingOfFavor";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private boolean usedThisCombat;

    public RingOfFavor() {
        super(ID, STRINGS.NAME, "img/cards/wylder/RingOfFavor.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 2;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.usedThisCombat) return;
        this.usedThisCombat = true;
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new RingOfFavorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void triggerOnExhaust() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) return;
        addToTop(new ApplyPowerAction(p, p, new RingOfFavorPower(p, -this.magicNumber), -this.magicNumber));
        addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, -this.magicNumber), -this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RingOfFavor();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        RingOfFavor copy = (RingOfFavor)super.makeStatEquivalentCopy();
        copy.usedThisCombat = this.usedThisCombat;
        return copy;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
