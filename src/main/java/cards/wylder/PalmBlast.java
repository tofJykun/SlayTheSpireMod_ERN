package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PalmBlastPower;

public class PalmBlast extends CustomCard {
    public static final String ID = "PalmBlast";
    private static final String IMG_PATH = "img/cards/wylder/PalmBlast.png";

    public PalmBlast() {
        super(ID, strings().NAME, IMG_PATH, 0, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new PalmBlastPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PalmBlast();
    }
}
