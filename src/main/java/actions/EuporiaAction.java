package actions;

import cards.duchess.Euporia;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class EuporiaAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final Euporia card;
    private final int remainingSegments;
    private final boolean allEnemies;

    public EuporiaAction(AbstractPlayer player, Euporia card, int segments, boolean allEnemies) {
        this.player = player;
        this.card = card;
        this.remainingSegments = segments;
        this.allEnemies = allEnemies;
        this.source = player;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        if (this.player == null || this.player.isDying || this.remainingSegments <= 0) {
            return;
        }

        addToTop(new EuporiaAction(this.player, this.card, this.remainingSegments - 1,
                this.allEnemies));
        if (this.allEnemies) {
            addToTop(new AllEnemiesSegmentAction(this.player, this.card));
        } else {
            addToTop(new RandomEnemySegmentAction(this.player, this.card));
        }
    }

    private static boolean canHit(AbstractMonster monster) {
        return monster != null && !monster.isDeadOrEscaped() && !monster.isDying
                && !monster.halfDead && monster.currentHealth > 0;
    }

    private static DamageInfo makeDamageInfo(AbstractPlayer player, Euporia card, int damage) {
        DamageInfo info = new DamageInfo((AbstractCreature)player, damage, card.damageTypeForTurn);
        info.output = damage;
        info.isModified = damage != card.baseDamage;
        return info;
    }

    private static class RandomEnemySegmentAction extends AbstractGameAction {
        private final AbstractPlayer player;
        private final Euporia card;

        private RandomEnemySegmentAction(AbstractPlayer player, Euporia card) {
            this.player = player;
            this.card = card;
        }

        @Override
        public void update() {
            AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(
                    null, true, AbstractDungeon.cardRandomRng);
            if (monster != null) {
                card.calculateCardDamage(monster);
                int damage = card.damage;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                        monster.hb.cX, monster.hb.cY, AttackEffect.SLASH_DIAGONAL));
                monster.damage(makeDamageInfo(player, card, damage));
                if (monster.isDying || monster.currentHealth <= 0) {
                    card.registerKill();
                }
            }
            this.isDone = true;
        }
    }

    private static class AllEnemiesSegmentAction extends AbstractGameAction {
        private final AbstractPlayer player;
        private final Euporia card;

        private AllEnemiesSegmentAction(AbstractPlayer player, Euporia card) {
            this.player = player;
            this.card = card;
        }

        @Override
        public void update() {
            ArrayList<AbstractMonster> targets = new ArrayList<>();
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (canHit(monster)) {
                    targets.add(monster);
                }
            }

            int[] damage = new int[AbstractDungeon.getMonsters().monsters.size()];
            for (int i = 0; i < damage.length; i++) {
                AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
                if (canHit(monster)) {
                    card.calculateCardDamage(monster);
                    damage[i] = card.damage;
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                            monster.hb.cX, monster.hb.cY, AttackEffect.SLASH_DIAGONAL,
                            i > 0));
                }
            }

            for (AbstractPower power : player.powers) {
                power.onDamageAllEnemies(damage);
            }
            for (int i = 0; i < targets.size(); i++) {
                AbstractMonster monster = targets.get(i);
                int index = AbstractDungeon.getMonsters().monsters.indexOf(monster);
                monster.damage(makeDamageInfo(player, card, damage[index]));
                if (monster.isDying || monster.currentHealth <= 0) {
                    card.registerKill();
                }
            }
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            this.isDone = true;
        }
    }
}
