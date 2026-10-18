package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;
import powers.WhiteNightPower;

public class WhiteNight extends CustomCard {
    public static final String ID = "WhiteNight";

    public WhiteNight() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/WhiteNight.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        isInnate = true;
        isEthereal = true;
        baseMagicNumber = magicNumber = 0;
        tags.add(CardTags.HEALING);
        FragileField.setBase(this, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WhiteNightPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override
    public AbstractCard makeCopy() { return new WhiteNight(); }
}
