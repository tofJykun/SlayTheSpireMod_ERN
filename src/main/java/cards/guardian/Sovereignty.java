package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SovereigntyPower;

public class Sovereignty extends CustomCard {
    public static final String ID = "Sovereignty";

    public Sovereignty() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/guardian/Sovereignty.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SovereigntyPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Sovereignty();
    }
}
