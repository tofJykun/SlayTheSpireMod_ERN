package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import relics.VoiceConduit;

public class PowerOfTheGreaterWill extends CustomCard {
    public static final String ID = "PowerOfTheGreaterWill";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/duchess/PowerOfTheGreaterWill.png";

    public PowerOfTheGreaterWill() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 0, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final int charges = this.magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                VoiceConduit conduit = (VoiceConduit)p.getRelic(VoiceConduit.ID);
                if (conduit == null) {
                    conduit = new VoiceConduit();
                    conduit.addCharges(charges);
                    conduit.instantObtain(p, p.relics.size(), true);
                } else {
                    conduit.addCharges(charges);
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerOfTheGreaterWill();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
