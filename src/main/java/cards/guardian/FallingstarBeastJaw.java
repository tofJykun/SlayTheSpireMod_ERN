package cards.guardian;

import actions.FallingstarBeastJawAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class FallingstarBeastJaw extends CustomCard {
    public static final String ID = "FallingstarBeastJaw";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public FallingstarBeastJaw() {
        super(ID, STRINGS.NAME, "img/cards/guardian/FallingstarBeastJaw.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FallingstarBeastJawAction(this, m, new DamageInfo(p, damage, damageTypeForTurn)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FallingstarBeastJaw();
    }
}
