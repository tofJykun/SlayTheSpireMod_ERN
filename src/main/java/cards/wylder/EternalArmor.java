package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.EternalArmorPower;

public class EternalArmor extends CustomCard {
    public static final String ID = "EternalArmor";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public EternalArmor() {
        super(ID, STRINGS.NAME, "img/cards/wylder/EternalArmor.png", 1, STRINGS.DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EternalArmorPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EternalArmor();
    }
}
