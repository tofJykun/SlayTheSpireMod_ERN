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
import powers.AssaultPower;

public class Assault extends CustomCard {
    public static final String ID = "Assault";
    private static final String IMG_PATH = "img/cards/guardian/Assault.png";
    private static final int COST = 1;
    private static final int ATTACKS_PER_TRIGGER = 3;
    private static final int VIGOR = 3;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    public Assault() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = VIGOR;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new AssaultPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Assault();
    }

    private void refreshDescription() {
        String description = getCardStrings().DESCRIPTION
                .replace("!M!", Integer.toString(ATTACKS_PER_TRIGGER))
                .replace("!MD!", Integer.toString(this.magicNumber));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            refreshDescription();
        }
    }
}
