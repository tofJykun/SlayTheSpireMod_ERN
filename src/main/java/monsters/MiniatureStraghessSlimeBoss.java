package monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class MiniatureStraghessSlimeBoss extends AbstractMonster {
    public static final String ID = "MiniatureStraghessSlimeBoss";
    private static final String SLIME_BOSS_ID = "SlimeBoss";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(SLIME_BOSS_ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String[] DIALOG = monsterStrings.DIALOG;

    private static final byte SLAM = 1;
    private static final byte PREP_SLAM = 2;
    private static final byte STICKY = 4;
    private static final String SLAM_NAME = MOVES[0];
    private static final String PREP_NAME = MOVES[1];
    private static final String STICKY_NAME = MOVES[3];

    private final int slamDmg;
    private boolean firstMove = true;

    public MiniatureStraghessSlimeBoss(int maxHp, float offsetX, float offsetY) {
        super(NAME, ID, Math.max(1, maxHp), 0.0F, -30.0F, 400.0F, 350.0F, null, offsetX, offsetY);
        this.type = EnemyType.NORMAL;
        this.maxHealth = Math.max(1, maxHp);
        this.currentHealth = this.maxHealth;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;

        int tackleDmg;
        if (AbstractDungeon.ascensionLevel >= 4) {
            tackleDmg = 10;
            this.slamDmg = 38;
        } else {
            tackleDmg = 9;
            this.slamDmg = 35;
        }
        this.damage.add(new DamageInfo(this, tackleDmg));
        this.damage.add(new DamageInfo(this, this.slamDmg));

        loadAnimation("images/monsters/theBottom/boss/slime/skeleton.atlas", "images/monsters/theBottom/boss/slime/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case STICKY:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SLIME_ATTACK"));
                int slimedCount = AbstractDungeon.ascensionLevel >= 19 ? 5 : 3;
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction((AbstractCard)new Slimed(), slimedCount));
                setMove(PREP_NAME, PREP_SLAM, Intent.UNKNOWN);
                break;
            case PREP_SLAM:
                playSfx();
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                setMove(SLAM_NAME, SLAM, Intent.ATTACK, this.damage.get(1).base);
                break;
            case SLAM:
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.animations.VFXAction((AbstractGameEffect)new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, new Color(0.1F, 1.0F, 0.1F, 0.0F))));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.POISON));
                setMove(STICKY_NAME, STICKY, Intent.STRONG_DEBUFF);
                break;
            default:
                setMove(STICKY_NAME, STICKY, Intent.STRONG_DEBUFF);
                break;
        }
    }

    private void playSfx() {
        int roll = MathUtils.random(1);
        AbstractDungeon.actionManager.addToBottom(new SFXAction(roll == 0 ? "VO_SLIMEBOSS_1A" : "VO_SLIMEBOSS_1B"));
    }

    @Override
    protected void getMove(int num) {
        if (this.firstMove) {
            this.firstMove = false;
            setMove(SLAM_NAME, SLAM, Intent.ATTACK, this.slamDmg);
        }
    }

    @Override
    public void die() {
        super.die();
        CardCrawlGame.sound.play("VO_SLIMEBOSS_2A");
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        CardCrawlGame.sound.play("VO_SLIMEBOSS_2A");
    }
}
