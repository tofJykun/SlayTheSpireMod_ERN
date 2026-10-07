package cards.guardian;

import actions.FurPluckingAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class FurPlucking extends CustomCard {
    public static final String ID = "FurPlucking";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public FurPlucking() {
        super(ID, STRINGS.NAME, "img/cards/guardian/FurPlucking.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 7;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new FurPluckingAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FurPlucking();
    }
}
