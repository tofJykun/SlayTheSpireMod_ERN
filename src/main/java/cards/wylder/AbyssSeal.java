package cards.wylder;

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
import powers.AbyssSealPower;

public class AbyssSeal extends CustomCard {
    public static final String ID = "AbyssSeal";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/wylder/AbyssSeal.png";
    private static final int COST = 1;
    private static final int ENERGY_PER_TURN = 1;
    private static final int UPGRADED_ENERGY_PER_TURN = 2;
    private static final int STRENGTH_LOSS = 3;

    public AbyssSeal() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = STRENGTH_LOSS;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = this.upgraded ? UPGRADED_ENERGY_PER_TURN : ENERGY_PER_TURN;
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new AbyssSealPower((AbstractCreature)p, energy), energy));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StrengthPower((AbstractCreature)p, -this.magicNumber), -this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AbyssSeal();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
