package powers;

import actions.BloodburnLoseHpAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BloodburnPower extends AbstractPower {
    public static final String POWER_ID = "BloodburnPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Color ICON_COLOR = new Color(70.0F / 255.0F, 10.0F / 255.0F, 60.0F / 255.0F, 1.0F);

    private final AbstractCreature source;

    public BloodburnPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        loadRegion("poison");
        updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
    }

    @Override
    public void updateDescription() {
        if (this.owner == null || this.owner.isPlayer) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot((AbstractGameAction)new BloodburnLoseHpAction(this.owner, this.source,
                    AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        Color color = ICON_COLOR.cpy();
        color.a = c.a;
        sb.setColor(color);
        if (Settings.isMobile) {
            sb.draw((TextureRegion)this.region48, x - this.region48.packedWidth / 2.0F,
                    y - this.region48.packedHeight / 2.0F, this.region48.packedWidth / 2.0F,
                    this.region48.packedHeight / 2.0F, this.region48.packedWidth, this.region48.packedHeight,
                    Settings.scale * 1.17F, Settings.scale * 1.17F, 0.0F);
        } else {
            sb.draw((TextureRegion)this.region48, x - this.region48.packedWidth / 2.0F,
                    y - this.region48.packedHeight / 2.0F, this.region48.packedWidth / 2.0F,
                    this.region48.packedHeight / 2.0F, this.region48.packedWidth, this.region48.packedHeight,
                    Settings.scale, Settings.scale, 0.0F);
        }
    }
}
