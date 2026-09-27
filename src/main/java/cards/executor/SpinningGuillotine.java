package cards.executor;

import actions.SpinningGuillotineAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SpinningGuillotine extends CustomCard {
    public static final String ID = "SpinningGuillotine";

    public SpinningGuillotine() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/SpinningGuillotine.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 28;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                new DamageInfo(p, damage, damageTypeForTurn),
                com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new SpinningGuillotineAction(p, m, this));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(8);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpinningGuillotine();
    }
}
