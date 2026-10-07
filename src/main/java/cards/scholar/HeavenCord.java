package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import relics.HermesBoots;

public class HeavenCord extends CustomCard {
    public static final String ID = "HeavenCord";

    public HeavenCord() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/HeavenCord.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final int charges = magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                HermesBoots boots = (HermesBoots)p.getRelic(HermesBoots.ID);
                if (boots == null) {
                    boots = new HermesBoots();
                    boots.addCharges(charges);
                    boots.instantObtain(p, p.relics.size(), true);
                } else {
                    boots.addCharges(charges);
                }
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new HeavenCord(); }
}
