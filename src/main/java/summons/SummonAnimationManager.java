package summons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

import java.util.HashMap;
import java.util.Map;

public class SummonAnimationManager {
    private static final float SUMMON_ALPHA = 0.5F;
    private static final Map<String, SummonAnimation> ANIMATIONS = new HashMap<>();

    private SummonAnimationManager() {
    }

    public static void render(AbstractPlayer player, String summonKey, SpriteBatch sb) {
        SummonAnimation animation = get(summonKey);
        if (animation != null) {
            animation.render(player, sb);
        }
    }

    public static void triggerAttack(String summonKey) {
        SummonAnimation animation = get(summonKey);
        if (animation != null) {
            animation.triggerAttack();
        }
    }

    private static SummonAnimation get(String summonKey) {
        SummonAnimation animation = ANIMATIONS.get(summonKey);
        if (animation == null) {
            animation = create(summonKey);
            if (animation != null) {
                ANIMATIONS.put(summonKey, animation);
            }
        }
        return animation;
    }

    private static SummonAnimation create(String summonKey) {
        if ("Helen".equals(summonKey)) {
            return new SummonAnimation(
                    "img/summons/Helen/flying.atlas",
                    "img/summons/Helen/flying.json",
                    "idle_flap",
                    null,
                    1.0F,
                    0.0F,
                    95.0F,
                    true);
        }
        if ("Frederick".equals(summonKey)) {
            return new SummonAnimation(
                    "img/summons/Frederick/skeleton.atlas",
                    "img/summons/Frederick/skeleton.json",
                    "waving",
                    null,
                    1.0F,
                    0.0F,
                    0.0F,
                    true);
        }
        if ("Sebastian".equals(summonKey)) {
            return new SummonAnimation(
                    "img/summons/Sebastian/skeleton.atlas",
                    "img/summons/Sebastian/skeleton.json",
                    "Idle",
                    "Attack_2",
                    1.0F,
                    0.0F,
                    -10.0F,
                    false);
        }
        if ("Doggo".equals(summonKey)) {
            return new SummonAnimation(
                    "img/summons/Doggo/skeleton.atlas",
                    "img/summons/Doggo/skeleton.json",
                    "Idle",
                    "Attack_2",
                    1.0F,
                    0.0F,
                    -10.0F,
                    false);
        }
        return null;
    }

    private static class SummonAnimation {
        private static final float ATTACK_DURATION = 0.35F;
        private static final float ATTACK_DISTANCE = 70.0F;

        private final Skeleton skeleton;
        private final AnimationStateData stateData;
        private final AnimationState state;
        private final String idleAnimation;
        private final String attackAnimation;
        private final float xOffset;
        private final float yOffset;
        private final boolean fallbackAttackMove;
        private float attackTimer = 0.0F;

        private SummonAnimation(String atlasPath, String skeletonPath, String idleAnimation, String attackAnimation,
                                float scale, float xOffset, float yOffset, boolean fallbackAttackMove) {
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
            SkeletonJson json = new SkeletonJson(atlas);
            json.setScale(Settings.renderScale / scale);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonPath));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);
            this.idleAnimation = idleAnimation;
            this.attackAnimation = attackAnimation;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.fallbackAttackMove = fallbackAttackMove;
            this.state.setAnimation(0, idleAnimation, true);
            if (attackAnimation != null) {
                this.stateData.setMix(attackAnimation, idleAnimation, 0.1F);
            }
        }

        private void triggerAttack() {
            if (this.attackAnimation != null) {
                this.state.setAnimation(0, this.attackAnimation, false);
                this.state.addAnimation(0, this.idleAnimation, true, 0.0F);
            }
            if (this.fallbackAttackMove) {
                this.attackTimer = ATTACK_DURATION;
            }
        }

        private void render(AbstractPlayer player, SpriteBatch sb) {
            float delta = Gdx.graphics.getDeltaTime();
            this.state.update(delta);
            this.state.apply(this.skeleton);
            this.skeleton.updateWorldTransform();

            float attackOffset = 0.0F;
            if (this.attackTimer > 0.0F) {
                float progress = 1.0F - this.attackTimer / ATTACK_DURATION;
                attackOffset = MathUtils.sin(progress * MathUtils.PI) * ATTACK_DISTANCE * Settings.scale;
                this.attackTimer -= delta;
                if (this.attackTimer < 0.0F) {
                    this.attackTimer = 0.0F;
                }
            }

            this.skeleton.setPosition(player.drawX + player.animX + this.xOffset * Settings.scale + attackOffset,
                    player.drawY + player.animY + this.yOffset * Settings.scale);
            this.skeleton.setColor(new Color(1.0F, 1.0F, 1.0F, SUMMON_ALPHA));
            this.skeleton.setFlip(true, false);

            sb.end();
            CardCrawlGame.psb.begin();
            AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        }
    }
}
