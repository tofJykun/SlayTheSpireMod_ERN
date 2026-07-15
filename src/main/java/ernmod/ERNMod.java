package ernmod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostEnergyRechargeSubscriber;
import basemod.interfaces.PostExhaustSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import basemod.interfaces.RelicGetSubscriber;
import cards.duchess.Defend_Duchess;
import cards.duchess.DynastFinesse;
import cards.duchess.Mortgage;
import cards.duchess.Strike_Duchess;
import cards.duchess.WaterfowlDance;
import cards.executor.Blending;
import cards.executor.AspectsOfCrucible;
import cards.executor.BlackFlame;
import cards.executor.BlackFlameBlade;
import cards.executor.BlackFlameRitual;
import cards.executor.BlackFlameTornado;
import cards.executor.DestinedDeath;
import cards.executor.Defend_Executor;
import cards.executor.LetFeastBegin;
import cards.executor.Numbness;
import cards.executor.PaintWorld;
import cards.executor.Strike_Executor;
import cards.guardian.Bonewheel;
import cards.guardian.ChargeForth;
import cards.guardian.Defend_Guardian;
import cards.guardian.Strike_Guardian;
import cards.guardian.WarmingStone;
import cards.guardian.WingsOfSalvation;
import cards.ironeye.Defend_Ironeye;
import cards.ironeye.CobCannon;
import cards.ironeye.Imitater;
import cards.ironeye.Strike_Ironeye;
import cards.raider.Defend_Raider;
import cards.raider.BloodDebt;
import cards.raider.BloodTax;
import cards.raider.BloodWall;
import cards.raider.Strike_Raider;
import cards.revenant.BlackButler;
import cards.recluse.BedOfMagic;
import cards.recluse.BellowingDragoncrest;
import cards.recluse.CarianPiercer;
import cards.recluse.CarianSlicer;
import cards.recluse.CometAzur;
import cards.recluse.CrystalScroll;
import cards.recluse.CrystalRingShield;
import cards.recluse.CrystalSoulSpear;
import cards.recluse.DarkBead;
import cards.recluse.DarkFog;
import cards.recluse.DarkOrb;
import cards.recluse.Defend_Recluse;
import cards.recluse.Dispelling;
import cards.recluse.Dodge;
import cards.recluse.Eagle;
import cards.recluse.FallControl;
import cards.recluse.FistfulOfAsh;
import cards.recluse.FoundingRainOfStars;
import cards.recluse.GlitstonePebble;
import cards.recluse.GreatResonantSoul;
import cards.recluse.GreatSoulDregs;
import cards.recluse.ElementalDefense;
import cards.recluse.HiddenBody;
import cards.recluse.HomingCrystalSoulmass;
import cards.recluse.LawOfRegression;
import cards.recluse.LingeringDragoncrest;
import cards.recluse.MagicBarrier;
import cards.recluse.MagicShield;
import cards.recluse.MorionBlade;
import cards.recluse.PrimalGlintstone;
import cards.recluse.ReignitedCinder;
import cards.recluse.ResonantSoul;
import cards.recluse.SoulBolt;
import cards.recluse.SoulbloodSong;
import cards.recluse.SoulFlash;
import cards.recluse.SoulGeyser;
import cards.recluse.SoulVortex;
import cards.recluse.SlumberingDragoncrest;
import cards.recluse.StoneClutch;
import cards.recluse.StrongMagicShield;
import cards.recluse.Strike_Recluse;
import cards.recluse.SwiftGlintstoneShard;
import cards.recluse.WhiteLightning;
import cards.recluse.Yearn;
import cards.revenant.Defend_Revenant;
import cards.revenant.Dirge;
import cards.revenant.DoggoXD;
import cards.revenant.Grovewort;
import cards.revenant.ImpatientBall;
import cards.revenant.Invoke;
import cards.revenant.PartingFlame;
import cards.revenant.PhantomSlash;
import cards.revenant.PowerWithin;
import cards.revenant.PumpkinHelm;
import cards.revenant.RedHairIcon;
import cards.revenant.RepeatingCrossbow;
import cards.revenant.Seance;
import cards.revenant.Strike_Revenant;
import cards.revenant.Undeath;
import cards.scholar.AntspurRapier;
import cards.scholar.Defend_Scholar;
import cards.scholar.PenglaiWorship;
import cards.scholar.PotionCoveredAshes;
import cards.scholar.Strike_Scholar;
import cards.scholar.Tyranny;
import cards.status.MagicEmber;
import cards.tempcards.CodeX;
import cards.tempcards.FadingPrimalGlintstone;
import cards.tempcards.PhantomDoggo;
import cards.tempcards.PhantomFrederick;
import cards.tempcards.PhantomHelen;
import cards.tempcards.PhantomSebastian;
import cards.wylder.BlueWhiteWoodenShield;
import cards.wylder.Barehanded;
import cards.wylder.BloodVeil;
import cards.wylder.NoxFlowingShieldTypeI;
import cards.wylder.NoxFlowingShieldTypeP;
import cards.wylder.OnslaughtStake;
import cards.wylder.Onikiri;
import cards.wylder.PitaBread;
import cards.wylder.RemoveArmor;
import cards.undertaker.Delirium;
import cards.undertaker.Defend_Undertaker;
import cards.undertaker.EyesWithin;
import cards.undertaker.ForbiddenSun;
import cards.undertaker.GreatSwampTome;
import cards.undertaker.OuterBoon;
import cards.undertaker.Strike_Undertaker;
import cards.undertaker.Zealotry;
import cards.wylder.ClawShot;
import cards.wylder.CrismonParma;
import cards.wylder.CrowQuills;
import cards.wylder.Defend_Wylder;
import cards.wylder.GrassCrestShield;
import cards.wylder.SmallShield;
import cards.wylder.Strike_Wylder;
import cards.wylder.SymbolOfAvarice;
import cards.wylder.TransientCurse;
import cards.wylder.Ubadachi;
import characters.Duchess;
import characters.Executor;
import characters.Guardian;
import characters.Ironeye;
import characters.Raider;
import characters.Recluse;
import characters.Revenant;
import characters.Scholar;
import characters.Undertaker;
import characters.Wylder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import general.BulkPotionQueue;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import powers.BellowingDragoncrestPower;
import powers.BlendingPower;
import powers.LingeringDragoncrestPower;
import powers.PaintWorldPower;
import powers.RedHairIconPower;
import powers.SlumberingDragoncrestPower;
import relics.Bagcraft;
import relics.BedOfChaos;
import relics.BequestOfSeath;
import relics.BorrowedLife;
import relics.CinderellaRosette;
import relics.CrucibleCodex;
import relics.DebtToBond;
import relics.FighterDestined;
import relics.FighterResolve;
import relics.HonorOfPinionfolk;
import relics.JosephForesight;
import relics.MagicCocktail;
import relics.Marking;
import relics.OolacileIvoryCatalyst;
import relics.MagnificentPoise;
import relics.OldGreataxe;
import relics.PrescientGlintEye;
import relics.ScrollOfLogan;
import relics.SacredTimber;
import relics.SeedbedCurse;
import relics.SoulForge;
import relics.SummonSpirit;
import relics.Suncatcher;
import relics.ToastyMittens;
import relics.Trance;
import relics.WhisperingEarring;
import relics.YearsEntwined;
import variables.CodeXBloodburnVariable;
import variables.CodeXPoisonVariable;
import variables.CodeXScarletRotVariable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

@SpireInitializer
public class ERNMod implements RelicGetSubscriber, PostPowerApplySubscriber, PostExhaustSubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber, EditCharactersSubscriber,
        PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        OnCardUseSubscriber, EditKeywordsSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber,
        PostEnergyRechargeSubscriber {
    private static final String MOD_BADGE = "img/UI/badge.png";
    private static final String RECLUSE_BUTTON = "img/charSelect/RecluseButton.png";
    private static final String RECLUSE_PORTRAIT = "img/charSelect/ReclusePortrait.jpg";
    private static final String WYLDER_BUTTON = "img/charSelect/WylderButton.png";
    private static final String WYLDER_PORTRAIT = "img/charSelect/WylderPortrait.jpg";
    private static final String GUARDIAN_BUTTON = "img/charSelect/GuardianButton.png";
    private static final String GUARDIAN_PORTRAIT = "img/charSelect/GuardianPortrait.jpg";
    private static final String IRONEYE_BUTTON = "img/charSelect/IroneyeButton.png";
    private static final String IRONEYE_PORTRAIT = "img/charSelect/IroneyePortrait.jpg";
    private static final String RAIDER_BUTTON = "img/charSelect/RaiderButton.png";
    private static final String RAIDER_PORTRAIT = "img/charSelect/RaiderPortrait.jpg";
    private static final String DUCHESS_BUTTON = "img/charSelect/DuchessButton.png";
    private static final String DUCHESS_PORTRAIT = "img/charSelect/DuchessPortrait.jpg";
    private static final String EXECUTOR_BUTTON = "img/charSelect/ExecutorButton.png";
    private static final String EXECUTOR_PORTRAIT = "img/charSelect/ExecutorPortrait.jpg";
    private static final String REVENANT_BUTTON = "img/charSelect/RevenantButton.png";
    private static final String REVENANT_PORTRAIT = "img/charSelect/RevenantPortrait.jpg";
    private static final String SCHOLAR_BUTTON = "img/charSelect/ScholarButton.png";
    private static final String SCHOLAR_PORTRAIT = "img/charSelect/ScholarPortrait.jpg";
    private static final String UNDERTAKER_BUTTON = "img/charSelect/UndertakerButton.png";
    private static final String UNDERTAKER_PORTRAIT = "img/charSelect/UndertakerPortrait.jpg";

    public static final Color RECLUSE_COLOR = CardHelper.getColor(70, 50, 70);
    public static final Color WYLDER_COLOR = CardHelper.getColor(60, 110, 140);
    public static final Color GUARDIAN_COLOR = CardHelper.getColor(100, 70, 50);
    public static final Color IRONEYE_COLOR = CardHelper.getColor(80, 90, 60);
    public static final Color DUCHESS_COLOR = CardHelper.getColor(240, 230, 50);
    public static final Color RAIDER_COLOR = CardHelper.getColor(90, 80, 90);
    public static final Color REVENANT_COLOR = CardHelper.getColor(150, 190, 220);
    public static final Color EXECUTOR_COLOR = CardHelper.getColor(170, 130, 120);
    public static final Color SCHOLAR_COLOR = CardHelper.getColor(60, 70, 70);
    public static final Color UNDERTAKER_COLOR = CardHelper.getColor(220, 210, 210);
    private static final RelicDiscoveryMode RELIC_DISCOVERY_MODE = RelicDiscoveryMode.FORCE_SEEN;
    private final ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
    public static ArrayList<AbstractCard> recyclecards = new ArrayList<>();

    public ERNMod() {
        BaseMod.subscribe((ISubscriber)this);
        BaseMod.addSaveField(BulkPotionQueue.SAVE_KEY, BulkPotionQueue.SAVE_FIELD);
        BaseMod.addDynamicVariable(new CodeXPoisonVariable());
        BaseMod.addDynamicVariable(new CodeXScarletRotVariable());
        BaseMod.addDynamicVariable(new CodeXBloodburnVariable());
        addNightfarerColor(AbstractCardEnum.Recluse_COLOR, RECLUSE_COLOR, "recluse");
        addNightfarerColor(AbstractCardEnum.Wylder_COLOR, WYLDER_COLOR, "wylder");
        addNightfarerColor(AbstractCardEnum.Guardian_COLOR, GUARDIAN_COLOR, "guardian");
        addNightfarerColor(AbstractCardEnum.Ironeye_COLOR, IRONEYE_COLOR, "ironeye");
        addNightfarerColor(AbstractCardEnum.Raider_COLOR, RAIDER_COLOR, "raider");
        addNightfarerColor(AbstractCardEnum.Duchess_COLOR, DUCHESS_COLOR, "duchess");
        addNightfarerColor(AbstractCardEnum.Executor_COLOR, EXECUTOR_COLOR, "executor");
        addNightfarerColor(AbstractCardEnum.Revenant_COLOR, REVENANT_COLOR, "revenant");
        addNightfarerColor(AbstractCardEnum.Scholar_COLOR, SCHOLAR_COLOR, "scholar");
        addNightfarerColor(AbstractCardEnum.Undertaker_COLOR, UNDERTAKER_COLOR, "undertaker");
    }

    public static void initialize() {
        new ERNMod();
    }

    private static void addNightfarerColor(AbstractCard.CardColor cardColor, Color renderColor, String assetName) {
        BaseMod.addColor(cardColor, renderColor, renderColor, renderColor, renderColor, renderColor,
                renderColor, renderColor,
                "img/512/bg_attack_" + assetName + "_s.png",
                "img/512/bg_skill_" + assetName + "_s.png",
                "img/512/bg_power_" + assetName + "_s.png",
                "img/512/card_" + assetName + "_orb.png",
                "img/1024/bg_attack_" + assetName + ".png",
                "img/1024/bg_skill_" + assetName + ".png",
                "img/1024/bg_power_" + assetName + ".png",
                "img/1024/card_" + assetName + "_orb.png",
                "img/UI_" + assetName + "/energyOrb.png");
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Recluse("Recluse"), RECLUSE_BUTTON, RECLUSE_PORTRAIT,
                ERNModClassEnum.Recluse_CLASS);
        BaseMod.addCharacter(new Wylder("Wylder"), WYLDER_BUTTON, WYLDER_PORTRAIT,
                ERNModClassEnum.Wylder_CLASS);
        BaseMod.addCharacter(new Guardian("Guardian"), GUARDIAN_BUTTON, GUARDIAN_PORTRAIT,
                ERNModClassEnum.Guardian_CLASS);
        BaseMod.addCharacter(new Ironeye("Ironeye"), IRONEYE_BUTTON, IRONEYE_PORTRAIT,
                ERNModClassEnum.Ironeye_CLASS);
        BaseMod.addCharacter(new Raider("Raider"), RAIDER_BUTTON, RAIDER_PORTRAIT,
                ERNModClassEnum.Raider_CLASS);
        BaseMod.addCharacter(new Duchess("Duchess"), DUCHESS_BUTTON, DUCHESS_PORTRAIT,
                ERNModClassEnum.Duchess_CLASS);
        BaseMod.addCharacter(new Executor("Executor"), EXECUTOR_BUTTON, EXECUTOR_PORTRAIT,
                ERNModClassEnum.Executor_CLASS);
        BaseMod.addCharacter(new Revenant("Revenant"), REVENANT_BUTTON, REVENANT_PORTRAIT,
                ERNModClassEnum.Revenant_CLASS);
        BaseMod.addCharacter(new Scholar("Scholar"), SCHOLAR_BUTTON, SCHOLAR_PORTRAIT,
                ERNModClassEnum.Scholar_CLASS);
        BaseMod.addCharacter(new Undertaker("Undertaker"), UNDERTAKER_BUTTON, UNDERTAKER_PORTRAIT,
                ERNModClassEnum.Undertaker_CLASS);
    }

    @Override
    public void receiveEditCards() {
        loadCardsToAdd();
        for (AbstractCard card : this.cardsToAdd) {
            BaseMod.addCard(card);
            markPreviewCardAsSeen(card);
        }
    }

    @Override
    public void receiveEditStrings() {
        String relic;
        String card;
        String power;
        String potion;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            card = "localization/ERNMod_cards-zh.json";
            relic = "localization/ERNMod_relics-zh.json";
            power = "localization/ERNMod_powers-zh.json";
            potion = "localization/ERNMod_potions-zh.json";
        } else {
            card = "localization/ERNMod_cards-eng.json";
            relic = "localization/ERNMod_relics-eng.json";
            power = "localization/ERNMod_powers-eng.json";
            potion = "localization/ERNMod_potions-eng.json";
        }

        String relicStrings = Gdx.files.internal(relic).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String cardStrings = Gdx.files.internal(card).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal(power).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String potionStrings = Gdx.files.internal(potion).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    private void loadCardsToAdd() {
        this.cardsToAdd.clear();
        this.cardsToAdd.add(new Strike_Recluse());
        this.cardsToAdd.add(new Defend_Recluse());
        this.cardsToAdd.add(new CarianSlicer());
        this.cardsToAdd.add(new MagicBarrier());
        this.cardsToAdd.add(new Strike_Wylder());
        this.cardsToAdd.add(new Defend_Wylder());
        this.cardsToAdd.add(new ClawShot());
        this.cardsToAdd.add(new SmallShield());
        this.cardsToAdd.add(new CrismonParma());
        this.cardsToAdd.add(new BlueWhiteWoodenShield());
        this.cardsToAdd.add(new GrassCrestShield());
        this.cardsToAdd.add(new TransientCurse());
        this.cardsToAdd.add(new NoxFlowingShieldTypeP());
        this.cardsToAdd.add(new NoxFlowingShieldTypeI());
        this.cardsToAdd.add(new OnslaughtStake());
        this.cardsToAdd.add(new PitaBread());
        this.cardsToAdd.add(new RemoveArmor());
        this.cardsToAdd.add(new Barehanded());
        this.cardsToAdd.add(new BloodVeil());
        this.cardsToAdd.add(new CrowQuills());
        this.cardsToAdd.add(new SymbolOfAvarice());
        this.cardsToAdd.add(new Ubadachi());
        this.cardsToAdd.add(new Onikiri());
        this.cardsToAdd.add(new Strike_Guardian());
        this.cardsToAdd.add(new Defend_Guardian());
        this.cardsToAdd.add(new cards.guardian.Whirlwind());
        this.cardsToAdd.add(new WarmingStone());
        this.cardsToAdd.add(new WingsOfSalvation());
        this.cardsToAdd.add(new ChargeForth());
        this.cardsToAdd.add(new Bonewheel());
        this.cardsToAdd.add(new Strike_Ironeye());
        this.cardsToAdd.add(new Defend_Ironeye());
        this.cardsToAdd.add(new CobCannon());
        this.cardsToAdd.add(new Imitater());
        this.cardsToAdd.add(new Strike_Raider());
        this.cardsToAdd.add(new Defend_Raider());
        this.cardsToAdd.add(new BloodWall());
        this.cardsToAdd.add(new BloodDebt());
        this.cardsToAdd.add(new BloodTax());
        this.cardsToAdd.add(new Strike_Duchess());
        this.cardsToAdd.add(new Defend_Duchess());
        this.cardsToAdd.add(new DynastFinesse());
        this.cardsToAdd.add(new cards.duchess.Restage());
        this.cardsToAdd.add(new WaterfowlDance());
        this.cardsToAdd.add(new Mortgage());
        this.cardsToAdd.add(new Strike_Executor());
        this.cardsToAdd.add(new Defend_Executor());
        this.cardsToAdd.add(new DestinedDeath());
        this.cardsToAdd.add(new BlackFlame());
        this.cardsToAdd.add(new BlackFlameTornado());
        this.cardsToAdd.add(new BlackFlameRitual());
        this.cardsToAdd.add(new BlackFlameBlade());
        this.cardsToAdd.add(new LetFeastBegin());
        this.cardsToAdd.add(new PaintWorld());
        this.cardsToAdd.add(new Blending());
        this.cardsToAdd.add(new AspectsOfCrucible());
        this.cardsToAdd.add(new Numbness());
        this.cardsToAdd.add(new Strike_Revenant());
        this.cardsToAdd.add(new Defend_Revenant());
        this.cardsToAdd.add(new PhantomSlash());
        this.cardsToAdd.add(new Invoke());
        this.cardsToAdd.add(new Dirge());
        this.cardsToAdd.add(new Grovewort());
        this.cardsToAdd.add(new PumpkinHelm());
        this.cardsToAdd.add(new RepeatingCrossbow());
        this.cardsToAdd.add(new BlackButler());
        this.cardsToAdd.add(new Seance());
        this.cardsToAdd.add(new Undeath());
        this.cardsToAdd.add(new PowerWithin());
        this.cardsToAdd.add(new PartingFlame());
        this.cardsToAdd.add(new RedHairIcon());
        this.cardsToAdd.add(new ImpatientBall());
        this.cardsToAdd.add(new DoggoXD());
        this.cardsToAdd.add(new Strike_Scholar());
        this.cardsToAdd.add(new Defend_Scholar());
        this.cardsToAdd.add(new PotionCoveredAshes());
        this.cardsToAdd.add(new AntspurRapier());
        this.cardsToAdd.add(new Tyranny());
        this.cardsToAdd.add(new PenglaiWorship());
        this.cardsToAdd.add(new Strike_Undertaker());
        this.cardsToAdd.add(new Defend_Undertaker());
        this.cardsToAdd.add(new Delirium());
        this.cardsToAdd.add(new OuterBoon());
        this.cardsToAdd.add(new EyesWithin());
        this.cardsToAdd.add(new GreatSwampTome());
        this.cardsToAdd.add(new ForbiddenSun());
        this.cardsToAdd.add(new Zealotry());
        this.cardsToAdd.add(new GlitstonePebble());
        this.cardsToAdd.add(new FallControl());
        this.cardsToAdd.add(new ResonantSoul());
        this.cardsToAdd.add(new SoulFlash());
        this.cardsToAdd.add(new SoulBolt());
        this.cardsToAdd.add(new SoulGeyser());
        this.cardsToAdd.add(new SoulVortex());
        this.cardsToAdd.add(new GreatResonantSoul());
        this.cardsToAdd.add(new SwiftGlintstoneShard());
        this.cardsToAdd.add(new HiddenBody());
        this.cardsToAdd.add(new CarianPiercer());
        this.cardsToAdd.add(new Yearn());
        this.cardsToAdd.add(new Dodge());
        this.cardsToAdd.add(new FistfulOfAsh());
        this.cardsToAdd.add(new MagicShield());
        this.cardsToAdd.add(new StrongMagicShield());
        this.cardsToAdd.add(new Dispelling());
        this.cardsToAdd.add(new StoneClutch());
        this.cardsToAdd.add(new FoundingRainOfStars());
        this.cardsToAdd.add(new DarkOrb());
        this.cardsToAdd.add(new DarkFog());
        this.cardsToAdd.add(new DarkBead());
        this.cardsToAdd.add(new CometAzur());
        this.cardsToAdd.add(new GreatSoulDregs());
        this.cardsToAdd.add(new HomingCrystalSoulmass());
        this.cardsToAdd.add(new CrystalScroll());
        this.cardsToAdd.add(new CrystalRingShield());
        this.cardsToAdd.add(new CrystalSoulSpear());
        this.cardsToAdd.add(new MorionBlade());
        this.cardsToAdd.add(new WhiteLightning());
        this.cardsToAdd.add(new ElementalDefense());
        this.cardsToAdd.add(new SoulbloodSong());
        this.cardsToAdd.add(new LingeringDragoncrest());
        this.cardsToAdd.add(new BellowingDragoncrest());
        this.cardsToAdd.add(new SlumberingDragoncrest());
        this.cardsToAdd.add(new Eagle());
        this.cardsToAdd.add(new PrimalGlintstone());
        this.cardsToAdd.add(new ReignitedCinder());
        this.cardsToAdd.add(new LawOfRegression());
        this.cardsToAdd.add(new FadingPrimalGlintstone());
        this.cardsToAdd.add(new PhantomHelen());
        this.cardsToAdd.add(new PhantomFrederick());
        this.cardsToAdd.add(new PhantomSebastian());
        this.cardsToAdd.add(new PhantomDoggo());
        this.cardsToAdd.add(new CodeX());
        this.cardsToAdd.add(new MagicEmber());
        this.cardsToAdd.add(new BedOfMagic());
    }

    private static void markPreviewCardAsSeen(AbstractCard card) {
        if (UnlockTracker.seenPref != null) {
            UnlockTracker.seenPref.putInteger(card.cardID, 1);
            UnlockTracker.seenPref.flush();
        }
        card.isSeen = true;
    }

    @Override
    public void receiveEditRelics() {
        addERNRelic(new MagicCocktail(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new BedOfChaos(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new BequestOfSeath(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new ScrollOfLogan(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new OolacileIvoryCatalyst(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new CinderellaRosette(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new relics.SixthSense(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new ToastyMittens(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new JosephForesight(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new HonorOfPinionfolk(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new Marking(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new FighterResolve(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new OldGreataxe(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new BorrowedLife(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new DebtToBond(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new FighterDestined(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new MagnificentPoise(), AbstractCardEnum.Duchess_COLOR);
        addERNRelic(new Suncatcher(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SeedbedCurse(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new CrucibleCodex(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SummonSpirit(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SoulForge(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new YearsEntwined(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SacredTimber(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new Bagcraft(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new Trance(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new PrescientGlintEye(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new WhisperingEarring(), AbstractCardEnum.Undertaker_COLOR);
    }

    private static void addERNRelic(AbstractRelic relic, AbstractCard.CardColor color) {
        BaseMod.addRelicToCustomPool(relic, color);
        applyRelicDiscoveryMode(relic);
    }

    private static void applyRelicDiscoveryMode(AbstractRelic relic) {
        if (relic == null || relic.relicId == null || UnlockTracker.relicSeenPref == null) {
            return;
        }
        if (RELIC_DISCOVERY_MODE == RelicDiscoveryMode.VANILLA) {
            relic.isSeen = UnlockTracker.isRelicSeen(relic.relicId);
            return;
        }

        boolean seen = RELIC_DISCOVERY_MODE == RelicDiscoveryMode.FORCE_SEEN;
        UnlockTracker.relicSeenPref.putInteger(relic.relicId, seen ? 1 : 0);
        UnlockTracker.relicSeenPref.flush();
        relic.isSeen = seen;

        AbstractRelic libraryRelic = RelicLibrary.getRelic(relic.relicId);
        if (libraryRelic != null) {
            libraryRelic.isSeen = seen;
        }
    }

    private enum RelicDiscoveryMode {
        FORCE_SEEN,
        FORCE_UNSEEN,
        VANILLA
    }

    @Override
    public void receiveRelicGet(AbstractRelic relic) {
        if (AbstractDungeon.player != null) {
            AbstractDungeon.shopRelicPool.remove("TinyHouse");
        }
    }

    @Override
    public void receivePostEnergyRecharge() {
        BedOfMagic.resetTurn();
        Iterator<AbstractCard> var1 = recyclecards.iterator();
        while (var1.hasNext()) {
            AbstractCard card = var1.next().makeStatEquivalentCopy();
            AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(card,
                    Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false, true, true));
        }
        recyclecards.clear();
    }

    @Override
    public void receivePostExhaust(AbstractCard c) {}

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower pow, AbstractCreature target, AbstractCreature owner) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SeedbedCurse.ID)) {
            ((SeedbedCurse)AbstractDungeon.player.getRelic(SeedbedCurse.ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(PaintWorldPower.POWER_ID)) {
            ((PaintWorldPower)AbstractDungeon.player.getPower(PaintWorldPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(BlendingPower.POWER_ID)) {
            ((BlendingPower)AbstractDungeon.player.getPower(BlendingPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(LingeringDragoncrestPower.POWER_ID)) {
            ((LingeringDragoncrestPower)AbstractDungeon.player.getPower(LingeringDragoncrestPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(RedHairIconPower.POWER_ID)) {
            ((RedHairIconPower)AbstractDungeon.player.getPower(RedHairIconPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(BellowingDragoncrestPower.POWER_ID)) {
            ((BellowingDragoncrestPower)AbstractDungeon.player.getPower(BellowingDragoncrestPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(SlumberingDragoncrestPower.POWER_ID)) {
            ((SlumberingDragoncrestPower)AbstractDungeon.player.getPower(SlumberingDragoncrestPower.POWER_ID))
                    .onPostPowerApply(pow, target, owner);
        }
    }

    @Override
    public void receivePowersModified() {}

    @Override
    public void receivePostDungeonInitialize() {
        BedOfMagic.resetCombat();
    }

    @Override
    public void receivePostDraw(AbstractCard arg0) {}

    @Override
    public void receiveEditKeywords() {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            BaseMod.addKeyword("\u53ec\u5524", new String[] { "\u53ec\u5524" },
                    "\u540d\u5b57\u4e2d\u6709\u53ec\u5524\u7684\u724c\u3002");
            BaseMod.addKeyword("\u57fa\u7840\u53ec\u5524", new String[] { "\u57fa\u7840\u53ec\u5524" },
                    " \u53ec\u5524\uff1a\u6d77\u4f26 \uff0c \u53ec\u5524\uff1a\u5f17\u96f7\u5fb7\u5229\u514b \u6216 \u53ec\u5524\uff1a\u585e\u5df4\u65af\u8482\u5b89\u3002");
            BaseMod.addKeyword("\u53ec\u5524\uff1a\u5927\u72d7", new String[] { "\u53ec\u5524\uff1a\u5927\u72d7" },
                    "\u83b7\u5f97 #b2 \u70b9\u683c\u6321\uff0c\u5c061\u5f20 #y\u5e7b\u5f71\u5927\u72d7 \u52a0\u5165\u4f60\u7684\u624b\u724c\uff0c\u5728\u4f60\u7684\u683c\u6321\u6d88\u5931\u65f6\u6d88\u6563\u3002");
            BaseMod.addKeyword("\u5e7b\u5f71\u5927\u72d7", new String[] { "\u5e7b\u5f71\u5927\u72d7" },
                    "\u4e00\u5f20 #y\u6d88\u8017 \u3001 #y\u865a\u65e0 \u7684\u7279\u6b8a\u653b\u51fb\u724c\u3002\u53ea\u6709\u5f53\u4f60\u62e5\u6709 #y\u53ec\u5524\uff1a\u5927\u72d7 \u65f6\u53ef\u4ee5\u6253\u51fa\u3002");
            BaseMod.addKeyword("\u5171\u51fb", new String[] { "\u5171\u51fb" },
                    "\u53ea\u6709\u5f53\u5bf9\u5e94 #y\u53ec\u5524 \u65f6\u53ef\u4ee5\u6253\u51fa\u3002");
            BaseMod.addKeyword("召唤：海伦", new String[] { "召唤：海伦" },
                    "获得 #b3 点格挡，将1张 #y幻影海伦 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：弗雷德利克", new String[] { "召唤：弗雷德利克" },
                    "获得 #b8 点格挡，将1张 #y幻影弗雷德利克 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：塞巴斯蒂安", new String[] { "召唤：塞巴斯蒂安" },
                    "获得 #b5 点格挡，将1张 #y幻影塞巴斯蒂安 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("南瓜头盔", new String[] { "南瓜头盔" },
                    "如果有 #y召唤：弗雷德利克 ， #y格挡 不再在你的回合开始时消失。");
            BaseMod.addKeyword("连射弩", new String[] { "连射弩" },
                    "如果有 #y召唤：海伦 ，打出的 #y幻影海伦 会额外打出 #b2 次。");
            BaseMod.addKeyword("黑执事", new String[] { "黑执事" },
                    "如果有 #y召唤：塞巴斯蒂安 ，在本回合 #y保留 你的手牌。");
            BaseMod.addKeyword("幻影海伦", new String[] { "幻影海伦" },
                    "一张 #y消耗 、 #y虚无 的特殊攻击牌。只有当你拥有 #y召唤：海伦 时可以打出。");
            BaseMod.addKeyword("幻影弗雷德利克", new String[] { "幻影弗雷德利克" },
                    "一张 #y消耗 、 #y虚无 的特殊攻击牌。只有当你拥有 #y召唤：弗雷德利克 时可以打出。");
            BaseMod.addKeyword("幻影塞巴斯蒂安", new String[] { "幻影塞巴斯蒂安" },
                    "一张 #y消耗 、 #y虚无 的特殊攻击牌。只有当你拥有 #y召唤：塞巴斯蒂安 时可以打出。");
            BaseMod.addKeyword("智力", new String[] { "智力" },
                    "魔法卡牌获得等同于智力层数的额外伤害、格挡和特殊数值。");
            BaseMod.addKeyword("源辉石", new String[] { "源辉石" },
                    "获得 #b1 点 #y智力 。升级后获得 #b2 点 #y智力 。");
            BaseMod.addKeyword("消逝源辉石", new String[] { "消逝源辉石" },
                    "在本回合获得 #b1 点 #y智力 。升级后获得 #b2 点 #y智力 。");
            BaseMod.addKeyword("魔法余烬", new String[] { "魔法余烬" },
                    "一张 #y状态牌 。可以打出并 #y消耗 。");
            BaseMod.addKeyword("魔法温床", new String[] { "魔法温床" },
                    "一张 #y保留 的技能牌。根据之前打出的三张牌的类型触发不同效果。");
            BaseMod.addKeyword("魔法", new String[] { "魔法" },
                    "效果中有“魔法”的卡牌。");
            BaseMod.addKeyword("结晶", new String[] { "结晶" },
                    "名字中有“结晶”的卡牌。");
            BaseMod.addKeyword("散装", new String[] { "散装" },
                    "在战斗结束时消失。");
            BaseMod.addKeyword("展翼", new String[] { "展翼" },
                    "将所受到的下几次伤害降低50%。每当你受到未被格挡的攻击伤害时，失去1层。");
            BaseMod.addKeyword("振翼", new String[] { "振翼" },
                    "将所受到的下几次伤害降低50%。每当你受到攻击伤害时，失去1层。");
            BaseMod.addKeyword("完美格挡", new String[] { "完美格挡" },
                    "持续若干回合。每当敌人的攻击伤害刚好击破你的格挡时，获得 #b6 点 #y活力 。");
            BaseMod.addKeyword("弹反", new String[] { "弹反" },
                    "持续若干回合。每当敌人的攻击伤害刚好击破你的格挡时，敌人下回合被 #y击晕 。");
            BaseMod.addKeyword("活力", new String[] { "活力" },
                    "你的下一次攻击造成额外伤害。");
            BaseMod.addKeyword("骤死", new String[] { "骤死" },
                    "回合结束时，如果骤死层数大于等于 #b10 ，会触发即死效果。");
            BaseMod.addKeyword("猩红腐败", new String[] { "猩红腐败" },
                    "在回合开始时，像 #y中毒 一样失去生命并减少层数，但会结算两次。");
            BaseMod.addKeyword("燃命", new String[] { "燃命" },
                    "在回合开始时，像 #y中毒 一样失去生命并减少层数，同时失去等量最大生命。");
            BaseMod.addKeyword("击晕", new String[] { "击晕" },
                    "敌人跳过当前行动，并在下回合继续原本意图。");
            BaseMod.addKeyword("虚血", new String[] { "虚血" },
                    "每打出一张攻击牌减少1层。在你的回合结束和战斗结束时，失去等同于层数的生命。");
            BaseMod.addKeyword("不屈", new String[] { "不屈" },
                    "阻止受到的生命值损伤，获得等同于生命值损伤数的 #y虚血 层数。");
            BaseMod.addKeyword("不屈+", new String[] { "不屈+" },
                    "阻止受到的生命值损伤，获得等同于生命值损伤数的 #y虚血+ 层数。");
            BaseMod.addKeyword("虚血+", new String[] { "虚血+" },
                    "每打出一张攻击牌减少2层。在你的回合结束和战斗结束时，失去等同于层数的生命。");
            BaseMod.addKeyword("长生不老药", new String[] { "长生不老药" },
                    "当你要被杀死时，免死并回复到最大生命值的 #b100% ，丢弃这瓶药水。在你的回合结束时，失去10金币。如果金币为0，丢弃这瓶药水。");
            BaseMod.addKeyword("灵视", new String[] { "灵视" },
                    "每回合的前若干次随机打出，改为选择一张牌将其免费打出。这次出牌仍视作随机打出。");
            BaseMod.addKeyword("\u7075\u529b", new String[] { "\u7075\u529b" },
                    "\u5e7b\u5f71\u724c\u7684\u6240\u6709\u5361\u9762\u6570\u5b57\u90fd\u589e\u52a0\u7b49\u540c\u4e8e #y\u7075\u529b \u7684\u6570\u503c\u3002");
            BaseMod.addKeyword("\u4fe1\u4ef0", new String[] { "\u4fe1\u4ef0" },
                    "\u5947\u672f \u5361\u724c\u83b7\u5f97\u6216\u5931\u53bb\u989d\u5916\u6570\u503c\u3002");
            BaseMod.addKeyword("\u5947\u672f", new String[] { "\u5947\u672f" },
                    "\u6548\u679c\u4e2d\u6709\u201c\u5947\u672f\u201d\u7684\u5361\u724c\u3002");
            BaseMod.addKeyword("\u51bb\u4f24", new String[] { "\u51bb\u4f24" },
                    "\u6bcf\u67091\u5c42 #y\u51bb\u4f24 \uff0c\u4ece #y\u653b\u51fb \u53d7\u5230\u7684\u4f24\u5bb3\u589e\u52a0 #b20% \u3002\u56de\u5408\u5f00\u59cb\u65f6\uff0c\u5c06 #y\u51bb\u4f24 \u5c42\u6570\u51cf\u5c11 #b1 \u3002");
            BaseMod.addKeyword("\u7761\u7720", new String[] { "\u7761\u7720" },
                    "\u6bcf\u67091\u5c42 #y\u7761\u7720 \uff0c\u76f8\u5f53\u4e8e\u5931\u53bb #b1 \u70b9 #y\u529b\u91cf \u3002\u56de\u5408\u7ed3\u675f\u65f6\uff0c\u5c06 #y\u7761\u7720 \u5c42\u6570\u51cf\u5c11 #b1 \u3002");
            BaseMod.addKeyword("\u53d1\u72c2", new String[] { "\u53d1\u72c2" },
                    "\u83b7\u5f97 #b10 \u5c42 #y\u53d1\u72c2 \u65f6\uff0c\u53d7\u5230 #b10% \u6700\u5927\u751f\u547d\u503c\u7684\u4f24\u5bb3\u3002\u5982\u679c\u662f\u654c\u4eba\uff0c\u5219\u989d\u5916\u5728\u5f53\u524d\u56de\u5408 #y\u51fb\u6655 \u3002");
            BaseMod.addKeyword("\u51fa\u8840", new String[] { "\u51fa\u8840" },
                    "\u83b7\u5f97 #b10 \u5c42 #y\u51fa\u8840 \u65f6\uff0c\u53d7\u5230 #b30% \u5f53\u524d\u751f\u547d\u503c\u7684\u4f24\u5bb3\u3002\u5982\u679c\u662f\u654c\u4eba\uff0c\u5219\u989d\u5916\u5931\u53bb #b30% \u5f53\u524d\u751f\u547d\u503c\u7684\u6700\u5927\u751f\u547d\u503c\u3002");
            BaseMod.addKeyword("\u524a\u97e7", new String[] { "\u524a\u97e7" },
                    "\u83b7\u5f97 #b10 \u5c42 #y\u524a\u97e7 \u65f6\uff0c\u654c\u4eba\u5728\u5f53\u524d\u56de\u5408 #y\u51fb\u6655 \uff0c\u73a9\u5bb6\u7ed3\u675f\u5f53\u524d\u56de\u5408\u3002");
            BaseMod.addKeyword("\u4fdd\u7559\u5361\u724c", new String[] { "\u4fdd\u7559\u5361\u724c" },
                    "\u5728\u4f60\u7684\u56de\u5408\u7ed3\u675f\u65f6\uff0c\u4fdd\u7559\u82e5\u5e72\u5f20\u724c\u3002");
            BaseMod.addKeyword("\u91d1\u5c5e\u5316", new String[] { "\u91d1\u5c5e\u5316" },
                    "\u5728\u4f60\u7684\u56de\u5408\u7ed3\u675f\u65f6\uff0c\u83b7\u5f97 #y\u683c\u6321 \u3002");
            BaseMod.addKeyword("异常", new String[] { "异常" },
                    " #y中毒 ， #y猩红腐败 ， #y燃命 。");
            BaseMod.addKeyword("异变", new String[] { "异变" },
                    " #y冻伤 ， #y睡眠 ， #y发狂 ， #y出血 。");
        } else {
            BaseMod.addKeyword("Summon", new String[] { "summon" },
                    "Cards whose names contain \"Summon\".");
            BaseMod.addKeyword("Basic Summon", new String[] { "basic summon" },
                    "Summon: Helen, Summon: Frederick, or Summon: Sebastian.");
            BaseMod.addKeyword("Summon: Helen", new String[] { "summon: helen" },
                    "Gain #b3 Block, add 1 #yPhantom #yHelen to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Frederick", new String[] { "summon: frederick" },
                    "Gain #b8 Block, add 1 #yPhantom #yFrederick to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Sebastian", new String[] { "summon: sebastian" },
                    "Gain #b5 Block, add 1 #yPhantom #ySebastian to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Doggo", new String[] { "summon: doggo" },
                    "Gain #b2 Block, add 1 #yPhantom #yDoggo to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Pumpkin Helm", new String[] { "pumpkin helm" },
                    "If you have #ySummon: #yFrederick, #yBlock is not removed at the start of your turn.");
            BaseMod.addKeyword("Repeating Crossbow", new String[] { "repeating crossbow" },
                    "If you have #ySummon: #yHelen, played #yPhantom #yHelen is played #b2 additional times.");
            BaseMod.addKeyword("Black Butler", new String[] { "black butler" },
                    "If you have #ySummon: #ySebastian, #yRetain your hand this turn.");
            BaseMod.addKeyword("Phantom Helen", new String[] { "phantom helen" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #yHelen.");
            BaseMod.addKeyword("Phantom Frederick", new String[] { "phantom frederick" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #yFrederick.");
            BaseMod.addKeyword("Phantom Sebastian", new String[] { "phantom sebastian" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #ySebastian.");
            BaseMod.addKeyword("Phantom Doggo", new String[] { "phantom doggo" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #yDoggo.");
            BaseMod.addKeyword("Slash", new String[] { "slash" },
                    "Can only be played while you have the corresponding #ySummon.");
            BaseMod.addKeyword("Intelligence", new String[] { "intelligence" },
                    "Magic cards gain additional damage, Block, and magic number equal to your Intelligence.");
            BaseMod.addKeyword("Primal Glintstone", new String[] { "primal glintstone" },
                    "Gain #b1 #yIntelligence. Upgraded: gain #b2 #yIntelligence.");
            BaseMod.addKeyword("Fading Primal Glintstone", new String[] { "fading primal glintstone" },
                    "Gain #b1 #yIntelligence this turn. Upgraded: gain #b2 #yIntelligence this turn.");
            BaseMod.addKeyword("Magic Ember", new String[] { "magic ember" },
                    "A #yStatus card. Can be played and #yExhausted.");
            BaseMod.addKeyword("Bed Of Magic", new String[] { "bed of magic" },
                    "A #yRetain Skill card. Its effect depends on the types of the last three cards you played.");
            BaseMod.addKeyword("Magic", new String[] { "magic" },
                    "Cards whose effects contain \"Magic\".");
            BaseMod.addKeyword("Crystal", new String[] { "crystal" },
                    "Cards whose names contain \"Crystal\".");
            BaseMod.addKeyword("Bulk", new String[] { "bulk" },
                    "Vanishes at the end of combat.");
            BaseMod.addKeyword("Soar", new String[] { "soar" },
                    "Reduces the next few instances of damage taken by 50%. Lose 1 stack whenever you take unblocked attack damage.");
            BaseMod.addKeyword("Hover", new String[] { "hover" },
                    "Reduces the next few instances of damage taken by 50%. Lose 1 stack whenever you take attack damage.");
            BaseMod.addKeyword("Perfect Guard", new String[] { "perfect guard" },
                    "Lasts for several turns. Whenever enemy attack damage exactly breaks your Block, gain #b6 #yVigor.");
            BaseMod.addKeyword("Parry", new String[] { "parry" },
                    "Lasts for several turns. Whenever enemy attack damage exactly breaks your Block, the enemy is #yStunned next turn.");
            BaseMod.addKeyword("Vigor", new String[] { "vigor" },
                    "Your next attack deals additional damage.");
            BaseMod.addKeyword("Deathblight", new String[] { "deathblight" },
                    "At the end of turn, if Deathblight is #b10 or higher, trigger an instant death effect.");
            BaseMod.addKeyword("Scarlet Rot", new String[] { "scarlet rot" },
                    "At the start of turn, lose HP and reduce stacks like #yPoison, but trigger twice.");
            BaseMod.addKeyword("Bloodburn", new String[] { "bloodburn" },
                    "At the start of turn, lose HP and reduce stacks like #yPoison, and lose the same amount of Max HP.");
            BaseMod.addKeyword("Stun", new String[] { "stun" },
                    "The enemy skips its current action and keeps the original intent for next turn.");
            BaseMod.addKeyword("Grey Health", new String[] { "grey health" },
                    "Reduced by 1 whenever you play an Attack. At the end of your turn and at the end of combat, lose HP equal to its stacks.");
            BaseMod.addKeyword("Unyielding", new String[] { "unyielding" },
                    "Prevent HP loss and gain #yGrey #yHealth equal to the HP loss prevented.");
            BaseMod.addKeyword("Unyielding+", new String[] { "unyielding+" },
                    "Prevent HP loss and gain #yGrey #yHealth+ equal to the HP loss prevented.");
            BaseMod.addKeyword("Grey Health+", new String[] { "grey health+" },
                    "Reduced by 2 whenever you play an Attack. At the end of your turn and at the end of combat, lose HP equal to its stacks.");
            BaseMod.addKeyword("Elixir of Life", new String[] { "elixir of life" },
                    "When you would die, survive and heal to #b100% of your Max HP, then discard this potion. At the end of your turn, lose 10 Gold. If you have 0 Gold, discard this potion.");
            BaseMod.addKeyword("Insight", new String[] { "insight" },
                    "For the first few random plays each turn, choose a card and play it for free instead. This still counts as a random play.");
            BaseMod.addKeyword("Spirit", new String[] { "spirit" },
                    "All card text numbers on Phantom cards are increased by your #ySpirit.");
            BaseMod.addKeyword("Faith", new String[] { "faith" },
                    "Thaumaturgy cards gain or lose additional value.");
            BaseMod.addKeyword("Thaumaturgy", new String[] { "thaumaturgy" },
                    "Cards whose effects contain \"Thaumaturgy\".");
            BaseMod.addKeyword("Frostbite", new String[] { "frostbite" },
                    "For each stack of #yFrostbite, take #b20% more damage from #yAttacks. At the start of turn, reduce #yFrostbite by #b1.");
            BaseMod.addKeyword("Sleep", new String[] { "sleep" },
                    "For each stack of #ySleep, this is equivalent to losing #b1 #yStrength. At the end of turn, reduce #ySleep by #b1.");
            BaseMod.addKeyword("Madness", new String[] { "madness" },
                    "When you gain #b10 #yMadness, take damage equal to #b10% of your Max HP. If this is an enemy, it is also #yStunned this turn.");
            BaseMod.addKeyword("Bloodloss", new String[] { "bloodloss" },
                    "When you gain #b10 #yBloodloss, take damage equal to #b30% of your current HP. If this is an enemy, it also loses Max HP equal to #b30% of its current HP.");
            BaseMod.addKeyword("Poise Break", new String[] { "poise break" },
                    "When this reaches #b10 #yPoise #yBreak, enemies are #yStunned this turn and the player ends the current turn.");
            BaseMod.addKeyword("Retain Cards", new String[] { "retain cards" },
                    "At the end of your turn, Retain a number of cards.");
            BaseMod.addKeyword("Metallicize", new String[] { "metallicize" },
                    "At the end of your turn, gain #yBlock.");
            BaseMod.addKeyword("Anomaly", new String[] { "anomaly", "anomalies" },
                    " #yPoison, #yScarlet #yRot, and #yBloodburn.");
            BaseMod.addKeyword("Aberration", new String[] { "aberration", "aberrations" },
                    " #yFrostbite, #ySleep, #yMadness, and #yBloodloss.");
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (BedOfMagic.ID.equals(abstractCard.cardID)) {
            return;
        }
        BedOfMagic.recordPlayedCard(abstractCard);
    }

    @Override
    public void receivePostBattle(AbstractRoom r) {
        BedOfMagic.resetCombat();
        removeBulkPotions();
    }

    private void removeBulkPotions() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (int i = 0; i < AbstractDungeon.player.potions.size(); i++) {
            AbstractPotion potion = AbstractDungeon.player.potions.get(i);
            if (potion != null && BulkPotionQueue.isBulkPotion(potion.ID)) {
                AbstractDungeon.player.potions.set(i, new PotionSlot(i));
            }
        }
    }

    @Override
    public void receivePostInitialize() {}

    class Keywords {
        Keyword[] keywords;
    }
}
