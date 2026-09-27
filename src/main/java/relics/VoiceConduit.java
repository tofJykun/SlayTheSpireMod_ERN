package relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class VoiceConduit extends CustomRelic implements CustomSavable<Integer> {
    public static final String ID = "VoiceConduit";
    public static final int HEAL = 6;
    private static final String IMG = "img/relics/duchess/VoiceConduit.png";
    private static final String OUTLINE = "img/relics/duchess/outline/VoiceConduit.png";
    private boolean pendingEntry;
    private int protectedFloor = -1;

    public VoiceConduit() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(OUTLINE),
                RelicTier.SPECIAL, LandingSound.MAGICAL);
        setCounter(0);
    }

    public void addCharges(int amount) {
        if (amount > 0) {
            setCounter((int)Math.min(Integer.MAX_VALUE, (long)this.counter + amount));
            flash();
        }
    }

    @Override
    public void setCounter(int counter) {
        this.counter = Math.max(0, counter);
        this.grayscale = this.counter == 0;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public boolean protectsRoomRoll() {
        // Post-combat reloads replay the room roll but skip both room-entry hooks.
        if (CardCrawlGame.loadingSave && CardCrawlGame.saveFile != null
                && CardCrawlGame.saveFile.post_combat) {
            return this.protectedFloor == AbstractDungeon.floorNum;
        }
        return this.counter > 0;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.pendingEntry = room instanceof EventRoom && this.counter > 0;
        this.protectedFloor = -1;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (this.pendingEntry) {
            this.pendingEntry = false;
            this.protectedFloor = AbstractDungeon.floorNum;
            setCounter(this.counter - 1);
            flash();
            AbstractDungeon.player.heal(HEAL);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + Math.max(0, this.counter) + this.DESCRIPTIONS[1]
                + HEAL + this.DESCRIPTIONS[2];
    }

    @Override
    public Integer onSave() {
        return this.protectedFloor;
    }

    @Override
    public void onLoad(Integer data) {
        this.protectedFloor = data == null ? -1 : data;
        this.pendingEntry = false;
        setCounter(this.counter);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VoiceConduit();
    }
}
