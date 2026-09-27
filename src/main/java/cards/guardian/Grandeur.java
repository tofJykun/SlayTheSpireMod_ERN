package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.GrandeurPower;

public class Grandeur extends CustomCard {
    public static final String ID = "Grandeur";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/guardian/Grandeur.png";
    private static final int COST = 2;
    private static final int ENERGY_PER_TURN = 1;
    private static final int UPGRADED_ENERGY_PER_TURN = 2;

    public Grandeur() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = this.upgraded ? UPGRADED_ENERGY_PER_TURN : ENERGY_PER_TURN;
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new GrandeurPower((AbstractCreature)p, energy), energy));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Grandeur();
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
