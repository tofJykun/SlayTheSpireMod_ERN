package characters;

import basemod.abstracts.CustomPlayer;
import cards.undertaker.Defend_Undertaker;
import cards.undertaker.Mace;
import cards.undertaker.SpiralhornShield;
import cards.undertaker.Strike_Undertaker;
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
import relics.Trance;

import java.util.ArrayList;

public class Undertaker extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Undertaker/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Undertaker/shoulder1.png";
    private static final String CORPSE = "img/char_Undertaker/fallen.png";
    private static final String STAND = "img/char_Undertaker/Undertaker.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_undertaker/EPanel/layer5.png", "img/UI_undertaker/EPanel/layer4.png",
            "img/UI_undertaker/EPanel/layer3.png", "img/UI_undertaker/EPanel/layer2.png",
            "img/UI_undertaker/EPanel/layer1.png", "img/UI_undertaker/EPanel/layer0.png",
            "img/UI_undertaker/EPanel/layer5d.png", "img/UI_undertaker/EPanel/layer4d.png",
            "img/UI_undertaker/EPanel/layer3d.png", "img/UI_undertaker/EPanel/layer2d.png",
            "img/UI_undertaker/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_undertaker/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Undertaker(String name) {
        super(name, ERNModClassEnum.Undertaker_CLASS, ERNEnergyOrb.purple("undertaker"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Undertaker/idle/skeleton.atlas", "img/char_Undertaker/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.7F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Undertaker.ID);
        retVal.add(Strike_Undertaker.ID);
        retVal.add(Strike_Undertaker.ID);
        retVal.add(Strike_Undertaker.ID);
        retVal.add(Defend_Undertaker.ID);
        retVal.add(Defend_Undertaker.ID);
        retVal.add(Defend_Undertaker.ID);
        retVal.add(Defend_Undertaker.ID);
        retVal.add(Mace.ID);
        retVal.add(SpiralhornShield.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Trance.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS
                        ? "信奉力量与序的修女，上帝掷骰子与否在一念之间。 NL 喜欢吃章鱼，不喜欢吃手指饼干。"
                        : "A nun who puts her faith in strength and order; whether God plays dice hinges on a single thought. NL She enjoys eating octopus but dislikes ladyfinger biscuits.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Undertaker_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.UNDERTAKER_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Undertaker();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.UNDERTAKER_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "送葬者" : "Undertaker";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Undertaker(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.UNDERTAKER_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT
        };
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }
}
