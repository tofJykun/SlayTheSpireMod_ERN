package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import general.CombatState;
import patches.AbstractCardEnum;

public class DeathFlare extends CustomCard {
    public static final String ID = "DeathFlare";

    public DeathFlare() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/DeathFlare.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 6;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (!CombatState.isInCombat()) {
                    return;
                }
                // Resolve the minion check after damage; addToTop preserves left-to-right order.
                for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                    AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
                    if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead
                            && monster.currentHealth > 0 && monster.hasPower(MinionPower.POWER_ID)) {
                        addToTop(new InstantKillAction(monster));
                    }
                }
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeathFlare();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
