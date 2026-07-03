package characters;

import basemod.abstracts.CustomPlayer;
import cards.recluse.CarianSlicer;
import cards.recluse.Defend_Recluse;
import cards.recluse.MagicBarrier;
import cards.recluse.Strike_Recluse;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import ernmod.ERNMod;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import relics.MagicCocktail;

import java.util.ArrayList;

public class Recluse extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Recluse/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Recluse/shoulder1.png";
    private static final String CORPSE = "img/char_Recluse/fallen.png";
    private static final String STAND = "img/char_Recluse/Recluse.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_recluse/EPanel/layer5.png", "img/UI_recluse/EPanel/layer4.png",
            "img/UI_recluse/EPanel/layer3.png", "img/UI_recluse/EPanel/layer2.png",
            "img/UI_recluse/EPanel/layer1.png", "img/UI_recluse/EPanel/layer0.png",
            "img/UI_recluse/EPanel/layer5d.png", "img/UI_recluse/EPanel/layer4d.png",
            "img/UI_recluse/EPanel/layer3d.png", "img/UI_recluse/EPanel/layer2d.png",
            "img/UI_recluse/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_recluse/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Recluse(String name) {
        super(name, ERNModClassEnum.Recluse_CLASS, ORB_TEXTURES, ORB_VFX, LAYER_SPEED, null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(STAND, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Recluse.ID);
        retVal.add(Strike_Recluse.ID);
        retVal.add(Strike_Recluse.ID);
        retVal.add(Defend_Recluse.ID);
        retVal.add(Defend_Recluse.ID);
        retVal.add(Defend_Recluse.ID);
        retVal.add(Defend_Recluse.ID);
        retVal.add(CarianSlicer.ID);
        retVal.add(MagicBarrier.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MagicCocktail.ID);
        UnlockTracker.markRelicAsSeen(MagicCocktail.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS ? "幽邃森林的女巫之一。 NL 能够熟练使用各种法术。" : "One of the witches of the deep forest, NL and is able to deftly cast all manner of spells.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Recluse_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.RECLUSE_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Recluse();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.RECLUSE_COLOR;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return ASCENSION_MAX_HP_LOSS;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
    }

    @Override
    public void updateOrb(int orbCount) {
        this.energyOrb.updateOrb(orbCount);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return null;
    }

    @Override
    public String getLocalizedCharacterName() {
        return Settings.language == Settings.GameLanguage.ZHS ? "隐士" : "Recluse";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Recluse(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.RECLUSE_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[0];
    }

    @Override
    public String getVampireText() {
        return null;
    }
}
