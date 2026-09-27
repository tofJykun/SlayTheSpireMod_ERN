package cards.scholar;

import actions.RollingSparksAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class RollingSparks extends CustomCard {
    public static final String ID = "RollingSparks";

    public RollingSparks() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/RollingSparks.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 20;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new RollingSparksAction(p, this.multiDamage, this.damageTypeForTurn));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(10);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RollingSparks();
    }
}
