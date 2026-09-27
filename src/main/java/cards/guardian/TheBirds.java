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
import powers.TheBirdsPower;

public class TheBirds extends CustomCard {
    public static final String ID = "TheBirds";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/guardian/TheBirds.png";
    private static final int COST = 3;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    public TheBirds() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = DAMAGE;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new TheBirdsPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheBirds();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE);
        }
    }
}
