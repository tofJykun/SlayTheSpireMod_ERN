package characters;

import basemod.abstracts.CustomPlayer;
import cards.wylder.Defend_Wylder;
import cards.wylder.SmallShield;
import cards.wylder.Strike_Wylder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import ernmod.ERNMod;
import general.ERNEnergyOrb;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import relics.SixthSense;

import java.util.ArrayList;

public class Wylder extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Wylder/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Wylder/shoulder1.png";
    private static final String CORPSE = "img/char_Wylder/fallen.png";
    private static final String STAND = "img/char_Wylder/Wylder.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_wylder/EPanel/layer5.png", "img/UI_wylder/EPanel/layer4.png",
            "img/UI_wylder/EPanel/layer3.png", "img/UI_wylder/EPanel/layer2.png",
            "img/UI_wylder/EPanel/layer1.png", "img/UI_wylder/EPanel/layer0.png",
            "img/UI_wylder/EPanel/layer5d.png", "img/UI_wylder/EPanel/layer4d.png",
            "img/UI_wylder/EPanel/layer3d.png", "img/UI_wylder/EPanel/layer2d.png",
            "img/UI_wylder/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_wylder/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Wylder(String name) {
        super(name, ERNModClassEnum.Wylder_CLASS, ERNEnergyOrb.red("wylder"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Wylder/idle/skeleton.atlas", "img/char_Wylder/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.6F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Wylder.ID);
        retVal.add(Strike_Wylder.ID);
        retVal.add(Strike_Wylder.ID);
        retVal.add(Strike_Wylder.ID);
        retVal.add(Defend_Wylder.ID);
        retVal.add(Defend_Wylder.ID);
        retVal.add(Defend_Wylder.ID);
        retVal.add(Defend_Wylder.ID);
        retVal.add(cards.wylder.ClawShot.ID);
        retVal.add(SmallShield.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(SixthSense.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS
                        ? "将思考融入战斗交互的骑士，一些力量只在解除封印时发挥。 NL 如果超绝操作被人忽略会脸色变黑。"
                        : "A knight who brings careful thought to every exchange in battle. Some of his powers only emerge when their seals are broken. NL His expression darkens when his masterful moves go unnoticed.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Wylder_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.WYLDER_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Wylder();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.WYLDER_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "追踪者" : "Wylder";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Wylder(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.WYLDER_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }
}
