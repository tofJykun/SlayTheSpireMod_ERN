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
import patches.AbstractCardEnum;
import powers.SmithboxPower;

public class Smithbox extends CustomCard {
    public static final String ID = "Smithbox";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/raider/Smithbox.png";
    private static final int COST = 2;
    private static final int HAMMERS_PER_TURN = 1;

    public Smithbox() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = HAMMERS_PER_TURN;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = makeHammerCard();
    }

    private AbstractCard makeHammerCard() {
        CraftmanHammer hammer = new CraftmanHammer();
        hammer.isEthereal = true;
        hammer.exhaust = true;
        return hammer;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SmithboxPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Smithbox();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
