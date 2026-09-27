package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.TotemStelaPower;

public class TotemStela extends CustomCard {
    public static final String ID = "TotemStela";
    private static final String IMG_PATH = "img/cards/raider/TotemStela.png";
    private static final int COST = 1;
    public static final int STRENGTH = 2;
    private static final int DAMAGE_REDUCTION = 50;

    public TotemStela() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = DAMAGE_REDUCTION;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH), STRENGTH));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new TotemStelaPower((AbstractCreature)p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TotemStela();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
