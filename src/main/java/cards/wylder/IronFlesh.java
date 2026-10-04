package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import patches.AbstractCardEnum;
import powers.PlatingPower;

public class IronFlesh extends CustomCard {
    public static final String ID = "IronFlesh";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public IronFlesh() {
        super(ID, STRINGS.NAME, "img/cards/wylder/IronFlesh.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public int getPlating() {
        return upgraded ? 6 : 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, -magicNumber), -magicNumber));
        addToBot(new ApplyPowerAction(p, p, new PlatingPower(p, getPlating()), getPlating()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new IronFlesh();
    }
}
