package cards.executor;

import actions.ReplayCardAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

public class InseparableSword extends CustomCard {
    public static final String ID = "InseparableSword";

    public InseparableSword() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/InseparableSword.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DrawCardAction(p, magicNumber));
        if (!purgeOnUse) {
            // Capture this play's card state before draw triggers can modify the original.
            final ReplayCardAction repeat = new ReplayCardAction(this, m, 1);
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    if (CombatState.isInCombat() && m != null && !m.isDeadOrEscaped()
                            && !m.isDying && !m.halfDead && m.hasPower(BloodburnPower.POWER_ID)) {
                        addToTop(repeat);
                    }
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(1);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new InseparableSword();
    }
}
