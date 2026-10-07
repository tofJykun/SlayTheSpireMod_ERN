package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;

public class UnalloyedGoldNeedle extends CustomCard {
    public static final String ID = "UnalloyedGoldNeedle";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public UnalloyedGoldNeedle() {
        super(ID, STRINGS.NAME, "img/cards/executor/UnalloyedGoldNeedle.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) {
                    return;
                }
                isDone = true;
                if (monster == null || monster.isDeadOrEscaped()) {
                    return;
                }
                // Queue removals in power-list order; do not mutate the list while scanning it.
                for (int i = monster.powers.size() - 1; i >= 0; i--) {
                    AbstractPower power = monster.powers.get(i);
                    if (power.type == AbstractPower.PowerType.BUFF) {
                        addToTop(new RemoveSpecificPowerAction(monster, player, power));
                    }
                }
            }
        });
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
        return new UnalloyedGoldNeedle();
    }
}
