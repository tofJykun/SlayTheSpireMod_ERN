package powers;

import actions.MushroomDoomAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class MushroomPower extends AbstractPower {
    public static final String POWER_ID = "MushroomPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    private static final int CLASS_AGARICOMYCETES = 0;
    private static final int ORDER_AGARICALES = 1;
    private static final int FAMILY_PLEUROTACEAE = 15;
    private static final int GENUS_PLEUROTUS = 20;
    private static final int SPECIES_ERYNGII = 24;
    private static final int MESSAGE_THE_UNIVERSE_APPROACHES = 29;

    private static final int STAGE_ORDER = 0;
    private static final int STAGE_FAMILY = 1;
    private static final int STAGE_GENUS = 2;
    private static final int STAGE_SPECIES = 3;

    private static final int[] ORDERS = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] FAMILIES = new int[] {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
    private static final int[] GENERA = new int[] {20, 21};
    private static final int[] SPECIES = new int[] {22, 23, 24, 25, 26, 27, 28};

    private int stage = STAGE_ORDER;
    private int descriptionIndex = CLASS_AGARICOMYCETES;
    private boolean readyToDoom = false;

    public MushroomPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (this.readyToDoom) {
            triggerDoom();
            return;
        }

        int selected = randomDifferentDescription(optionsForStage());
        this.descriptionIndex = selected;
        advanceStageIfNeeded(selected);
        updateDescription();
    }

    private int[] optionsForStage() {
        switch (this.stage) {
            case STAGE_FAMILY:
                return FAMILIES;
            case STAGE_GENUS:
                return GENERA;
            case STAGE_SPECIES:
                return SPECIES;
            case STAGE_ORDER:
            default:
                return ORDERS;
        }
    }

    private int randomDifferentDescription(int[] options) {
        int availableCount = 0;
        int[] available = new int[options.length];
        for (int option : options) {
            if (option != this.descriptionIndex) {
                available[availableCount] = option;
                availableCount++;
            }
        }
        if (availableCount <= 0) {
            return this.descriptionIndex;
        }
        return available[AbstractDungeon.cardRandomRng.random(availableCount - 1)];
    }

    private void advanceStageIfNeeded(int selected) {
        if (this.stage == STAGE_ORDER && selected == ORDER_AGARICALES) {
            this.stage = STAGE_FAMILY;
        } else if (this.stage == STAGE_FAMILY && selected == FAMILY_PLEUROTACEAE) {
            this.stage = STAGE_GENUS;
        } else if (this.stage == STAGE_GENUS && selected == GENUS_PLEUROTUS) {
            this.stage = STAGE_SPECIES;
        } else if (this.stage == STAGE_SPECIES && selected == SPECIES_ERYNGII) {
            this.readyToDoom = true;
        }
    }

    private void triggerDoom() {
        this.readyToDoom = false;
        flash();
        if (AbstractDungeon.player != null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX,
                    AbstractDungeon.player.dialogY, 3.0F, DESCRIPTIONS[MESSAGE_THE_UNIVERSE_APPROACHES], true));
        }
        addToBot((AbstractGameAction)new MushroomDoomAction(this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[this.descriptionIndex];
    }
}
