package characters;

import basemod.abstracts.CustomPlayer;
import cards.duchess.Defend_Duchess;
import cards.duchess.Strike_Duchess;
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
import ernmod.ERNMod;
import general.ERNEnergyOrb;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import relics.MagnificentPoise;

import java.util.ArrayList;

public class Duchess extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Duchess/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Duchess/shoulder1.png";
    private static final String CORPSE = "img/char_Duchess/fallen.png";
    private static final String STAND = "img/char_Duchess/Duchess.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_duchess/EPanel/layer5.png", "img/UI_duchess/EPanel/layer4.png",
            "img/UI_duchess/EPanel/layer3.png", "img/UI_duchess/EPanel/layer2.png",
            "img/UI_duchess/EPanel/layer1.png", "img/UI_duchess/EPanel/layer0.png",
            "img/UI_duchess/EPanel/layer5d.png", "img/UI_duchess/EPanel/layer4d.png",
            "img/UI_duchess/EPanel/layer3d.png", "img/UI_duchess/EPanel/layer2d.png",
            "img/UI_duchess/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_duchess/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Duchess(String name) {
        super(name, ERNModClassEnum.Duchess_CLASS, ERNEnergyOrb.green("duchess"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Duchess/idle/skeleton.atlas", "img/char_Duchess/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.9F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Duchess.ID);
        retVal.add(Strike_Duchess.ID);
        retVal.add(Strike_Duchess.ID);
        retVal.add(Strike_Duchess.ID);
        retVal.add(Defend_Duchess.ID);
        retVal.add(Defend_Duchess.ID);
        retVal.add(Defend_Duchess.ID);
        retVal.add(Defend_Duchess.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MagnificentPoise.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS ? "行动敏捷、能够重演近期行动的渡夜者。" : "An agile Nightfarer who reprises recent actions.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Duchess_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.DUCHESS_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Duchess();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.DUCHESS_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "女爵" : "Duchess";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Duchess(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.DUCHESS_COLOR;
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
