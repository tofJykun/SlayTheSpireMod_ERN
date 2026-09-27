package characters;

import basemod.abstracts.CustomPlayer;
import cards.ironeye.Defend_Ironeye;
import cards.ironeye.Marking;
import cards.ironeye.Strike_Ironeye;
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
import relics.EagleEye;

import java.util.ArrayList;

public class Ironeye extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Ironeye/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Ironeye/shoulder1.png";
    private static final String CORPSE = "img/char_Ironeye/fallen.png";
    private static final String STAND = "img/char_Ironeye/Ironeye.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_ironeye/EPanel/layer5.png", "img/UI_ironeye/EPanel/layer4.png",
            "img/UI_ironeye/EPanel/layer3.png", "img/UI_ironeye/EPanel/layer2.png",
            "img/UI_ironeye/EPanel/layer1.png", "img/UI_ironeye/EPanel/layer0.png",
            "img/UI_ironeye/EPanel/layer5d.png", "img/UI_ironeye/EPanel/layer4d.png",
            "img/UI_ironeye/EPanel/layer3d.png", "img/UI_ironeye/EPanel/layer2d.png",
            "img/UI_ironeye/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_ironeye/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Ironeye(String name) {
        super(name, ERNModClassEnum.Ironeye_CLASS, ERNEnergyOrb.red("ironeye"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Ironeye/idle/skeleton.atlas", "img/char_Ironeye/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.6F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Ironeye.ID);
        retVal.add(Strike_Ironeye.ID);
        retVal.add(Strike_Ironeye.ID);
        retVal.add(Strike_Ironeye.ID);
        retVal.add(Defend_Ironeye.ID);
        retVal.add(Defend_Ironeye.ID);
        retVal.add(Defend_Ironeye.ID);
        retVal.add(Defend_Ironeye.ID);
        retVal.add(Marking.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(EagleEye.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS
                        ? "以寻宝谋生的机构杀手，金钱与装备又将投入战斗。 NL 紫玉米以月光为食，因而黑夜必须留下。"
                        : "An assassin of the Fellowship who makes a living hunting treasure and puts his earnings and equipment back into battle. NL Purple corn feeds on moonlight, so the night must remain.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Ironeye_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.IRONEYE_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Ironeye();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.IRONEYE_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "铁之眼" : "Ironeye";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Ironeye(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.IRONEYE_COLOR;
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
