package cards.executor;

import actions.SwordOfMilosAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SwordOfMilos extends CustomCard {
    public static final String ID = "SwordOfMilos";

    public SwordOfMilos() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/SwordOfMilos.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 22;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SwordOfMilosAction(p, m, new DamageInfo(p, damage, damageTypeForTurn)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(6);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordOfMilos();
    }
}
