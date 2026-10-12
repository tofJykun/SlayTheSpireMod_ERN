package cards.ironeye;

import actions.LushAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Lush extends CustomCard {
    public static final String ID = "Lush";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Lush() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/Lush.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LushAction(p, uuid, !purgeOnUse));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Lush();
    }
}
