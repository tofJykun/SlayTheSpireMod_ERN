package characters;

import basemod.abstracts.CustomPlayer;
import cards.guardian.Defend_Guardian;
import cards.guardian.Strike_Guardian;
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
import relics.HonorOfPinionfolk;

import java.util.ArrayList;

public class Guardian extends CustomPlayer {
    private static final int ENERGY_PER_TURN = 3;
    private static final int STARTING_HP = 75;
    private static final int MAX_HP = 75;
    private static final int STARTING_GOLD = 99;
    private static final int HAND_SIZE = 0;
    private static final int ASCENSION_MAX_HP_LOSS = 5;

    private static final String SHOULDER_2 = "img/char_Guardian/shoulder2.png";
    private static final String SHOULDER_1 = "img/char_Guardian/shoulder1.png";
    private static final String CORPSE = "img/char_Guardian/fallen.png";
    private static final String STAND = "img/char_Guardian/Guardian.png";
    private static final String[] ORB_TEXTURES = new String[] {
            "img/UI_guardian/EPanel/layer5.png", "img/UI_guardian/EPanel/layer4.png",
            "img/UI_guardian/EPanel/layer3.png", "img/UI_guardian/EPanel/layer2.png",
            "img/UI_guardian/EPanel/layer1.png", "img/UI_guardian/EPanel/layer0.png",
            "img/UI_guardian/EPanel/layer5d.png", "img/UI_guardian/EPanel/layer4d.png",
            "img/UI_guardian/EPanel/layer3d.png", "img/UI_guardian/EPanel/layer2d.png",
            "img/UI_guardian/EPanel/layer1d.png"
    };
    private static final String ORB_VFX = "img/UI_guardian/energyVFX.png";
    private static final float[] LAYER_SPEED = new float[] {
            -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F
    };

    public Guardian(String name) {
        super(name, ERNModClassEnum.Guardian_CLASS, ERNEnergyOrb.blue("guardian"), null, null);
        this.dialogX = this.drawX;
        this.dialogY = this.drawY + 220.0F * Settings.scale;
        initializeClass(null, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
        loadAnimation("img/char_Guardian/idle/skeleton.atlas", "img/char_Guardian/idle/skeleton.json", 1.0F);
        this.state.setAnimation(0, "Idle", true).setTimeScale(0.9F);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Strike_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(Defend_Guardian.ID);
        retVal.add(cards.guardian.GuardianWhirlwind.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(HonorOfPinionfolk.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(),
                Settings.language == Settings.GameLanguage.ZHS
                        ? "尽管羽翼受到诅咒，仍能借助天空战斗的群体。 NL 合翼卫，可以举盾一整天。"
                        : "A flock that harnesses the skies in battle despite its cursed wings. NL A guardian with folded wings who can keep his shield raised all day.",
                STARTING_HP, MAX_HP, HAND_SIZE, STARTING_GOLD, ASCENSION_MAX_HP_LOSS,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.Guardian_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return ERNMod.GUARDIAN_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Guardian();
    }

    @Override
    public Color getCardTrailColor() {
        return ERNMod.GUARDIAN_COLOR;
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
        return Settings.language == Settings.GameLanguage.ZHS ? "守护者" : "Guardian";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Guardian(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return SpireHeart.DESCRIPTIONS[10];
    }

    @Override
    public Color getSlashAttackColor() {
        return ERNMod.GUARDIAN_COLOR;
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
        return Vampires.DESCRIPTIONS[0];
    }
}
