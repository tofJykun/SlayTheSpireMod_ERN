package characters;

import basemod.abstracts.CustomPlayer;
import cards.scholar.Defend_Scholar;
import cards.scholar.PotionCoveredAshes;
import cards.scholar.Strike_Scholar;
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
import relics.Bagcraft;

import java.util.ArrayList;

public class Scholar extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Scholar/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Scholar/shoulder1.png";
    private static final String CORPSE = "img/char_Scholar/fallen.png";
    private static final String STAND = "img/char_Scholar/Scholar.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_scholar/EPanel/layer5.png", "img/UI_scholar/EPanel/layer4.png",
            "img/UI_scholar/EPanel/layer3.png", "img/UI_scholar/EPanel/layer2.png",
            "img/UI_scholar/EPanel/layer1.png", "img/UI_scholar/EPanel/layer0.png",
            "img/UI_scholar/EPanel/layer5d.png", "img/UI_scholar/EPanel/layer4d.png",
            "img/UI_scholar/EPanel/layer3d.png", "img/UI_scholar/EPanel/layer2d.png",
            "img/UI_scholar/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_scholar/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Scholar(String name) {
        super(name, ERNModClassEnum.Scholar_CLASS, ERNEnergyOrb.blue("scholar"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Scholar/idle/skeleton.atlas", "img/char_Scholar/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.9F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Scholar.ID);
        retVal.add(Strike_Scholar.ID);
        retVal.add(Strike_Scholar.ID);
        retVal.add(Strike_Scholar.ID);
        retVal.add(Defend_Scholar.ID);
        retVal.add(Defend_Scholar.ID);
        retVal.add(Defend_Scholar.ID);
        retVal.add(Defend_Scholar.ID);
        retVal.add(PotionCoveredAshes.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Bagcraft.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS ? "分析战场并寻找破绽的学者。" : "A calculating Nightfarer who studies the battlefield.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Scholar_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.SCHOLAR_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Scholar();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.SCHOLAR_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "学者" : "Scholar";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Scholar(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.SCHOLAR_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getVampireText() {
        return null;
    }
}
