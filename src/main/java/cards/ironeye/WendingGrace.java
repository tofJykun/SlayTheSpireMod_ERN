package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;

public class WendingGrace extends CustomCard {
    public static final String ID = "WendingGrace";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public WendingGrace() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/WendingGrace.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 25;
        this.tags.add(CardTags.HEALING);
        FragileField.setBase(this, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(10);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WendingGrace();
    }
}
