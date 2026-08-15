package ernmod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostEnergyRechargeSubscriber;
import basemod.interfaces.PostExhaustSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import basemod.interfaces.RelicGetSubscriber;
import cards.duchess.CeruleanburstCrystal;
import cards.duchess.ChillingMist;
import cards.duchess.Defend_Duchess;
import cards.duchess.DarkSilverTracer;
import cards.duchess.DynastFinesse;
import cards.duchess.GoldTracer;
import cards.duchess.Mortgage;
import cards.duchess.SellswordTwinblades;
import cards.duchess.Strike_Duchess;
import cards.duchess.WaterfowlDance;
import cards.duchess.WildStrikes;
import cards.executor.Blending;
import cards.executor.AriandelBlood;
import cards.executor.AspectsOfCrucible;
import cards.executor.BlackFlame;
import cards.executor.BlackFlameBlade;
import cards.executor.BlackFlameRitual;
import cards.executor.BlackFlameTornado;
import cards.executor.BloodboonRitual;
import cards.executor.BloodhoundFang;
import cards.executor.DestinedDeath;
import cards.executor.Defend_Executor;
import cards.executor.FrenziedBurst;
import cards.executor.HoarfrostStomp;
import cards.executor.LetFeastBegin;
import cards.executor.Numbness;
import cards.executor.PaintWorld;
import cards.executor.PowerOfErdtree;
import cards.executor.RiversOfBlood;
import cards.executor.Seppuku;
import cards.executor.StTrinaSword;
import cards.executor.Strike_Executor;
import cards.executor.UnendurableFrenzy;
import cards.guardian.Bonewheel;
import cards.guardian.BarricadeShield;
import cards.guardian.ChargeForth;
import cards.guardian.Defend_Guardian;
import cards.guardian.SpikedShield;
import cards.guardian.Strike_Guardian;
import cards.guardian.WarmingStone;
import cards.guardian.WingsOfSalvation;
import cards.ironeye.CeruleanwhorlBubble;
import cards.ironeye.Defend_Ironeye;
import cards.ironeye.FellowshipAtt;
import cards.ironeye.CobCannon;
import cards.ironeye.Imitater;
import cards.ironeye.PetrifiedSomething;
import cards.ironeye.SmoothSilkyStone;
import cards.ironeye.StringSecret;
import cards.ironeye.Strike_Ironeye;
import cards.raider.Defend_Raider;
import cards.raider.BloodDebt;
import cards.raider.BloodTax;
import cards.raider.BloodWall;
import cards.raider.BloodfiendArm;
import cards.raider.BleedStone;
import cards.raider.Boltstone;
import cards.raider.CraftmanHammer;
import cards.raider.DarknightStone;
import cards.raider.DemonTitanite;
import cards.raider.DragonScale;
import cards.raider.Faintstone;
import cards.raider.FiredrakeStone;
import cards.raider.MagicStone;
import cards.raider.OldMundaneStone;
import cards.raider.Palestone;
import cards.raider.Pickaxe;
import cards.raider.Kick;
import cards.raider.PoisonStone;
import cards.raider.PrismStone;
import cards.raider.RawStone;
import cards.raider.TitaniteChunk;
import cards.raider.TitaniteSlab;
import cards.raider.TitaniteShard;
import cards.raider.TwinklingTitanite;
import cards.raider.DuelistGreataxe;
import cards.raider.Strike_Raider;
import cards.revenant.BlackButler;
import cards.recluse.BedOfMagic;
import cards.recluse.BellowingDragoncrest;
import cards.recluse.AmbushShard;
import cards.recluse.ArchiveOccult;
import cards.recluse.Bloodbite;
import cards.recluse.BloodSigil;
import cards.recluse.CannonOfHaima;
import cards.recluse.CarianPiercer;
import cards.recluse.CarianSlicer;
import cards.recluse.Chameleon;
import cards.recluse.CollectAffinity;
import cards.recluse.CometAzur;
import cards.recluse.Crystallization;
import cards.recluse.CrystalScroll;
import cards.recluse.CrystalRingShield;
import cards.recluse.CrystalScrap;
import cards.recluse.CrystalSoulSpear;
import cards.recluse.DarkBead;
import cards.recluse.DarkFog;
import cards.recluse.DarkHand;
import cards.recluse.DarkOrb;
import cards.recluse.Defend_Recluse;
import cards.recluse.Dispelling;
import cards.recluse.Dodge;
import cards.recluse.Eagle;
import cards.recluse.FallControl;
import cards.recluse.FarronFlashsword;
import cards.recluse.FistfulOfAsh;
import cards.recluse.FoundingRainOfStars;
import cards.recluse.GlintbladePhalanx;
import cards.recluse.GlitstonePebble;
import cards.recluse.GlintstoneCrown;
import cards.recluse.GravityCrystal;
import cards.recluse.GreatResonantSoul;
import cards.recluse.GreatSoulDregs;
import cards.recluse.ElementalDefense;
import cards.recluse.HiddenBody;
import cards.recluse.HiddenWeapon;
import cards.recluse.HomingCrystalSoulmass;
import cards.recluse.Ictarus;
import cards.recluse.LapisLazuliGeode;
import cards.recluse.LawOfRegression;
import cards.recluse.LingeringDragoncrest;
import cards.recluse.MagicBarrier;
import cards.recluse.MagicKinship;
import cards.recluse.MagicRecycle;
import cards.recluse.MendicantStaff;
import cards.recluse.MorionBlade;
import cards.recluse.PrimalGlintstone;
import cards.recluse.PowerOfDragonlord;
import cards.recluse.PunishmentBriars;
import cards.recluse.Quasicrystal;
import cards.recluse.QuellStorm;
import cards.recluse.ReignitedCinder;
import cards.recluse.Replication;
import cards.recluse.ResonantSoul;
import cards.recluse.RockBlaster;
import cards.recluse.SeekGuidance;
import cards.recluse.SoulBolt;
import cards.recluse.SoulbloodSong;
import cards.recluse.SoulFlash;
import cards.recluse.SoulGeyser;
import cards.recluse.SoulVessel;
import cards.recluse.SoulVortex;
import cards.recluse.SlumberingDragoncrest;
import cards.recluse.SinBriars;
import cards.recluse.StoneClutch;
import cards.recluse.StarcallerCry;
import cards.recluse.StrongMagicShield;
import cards.recluse.Strike_Recluse;
import cards.recluse.SwiftGlintstoneShard;
import cards.recluse.TheGnawling;
import cards.recluse.TwilightHerb;
import cards.recluse.UnleashMagic;
import cards.recluse.WhiteLightning;
import cards.recluse.Yearn;
import cards.revenant.Defend_Revenant;
import cards.revenant.Dirge;
import cards.revenant.DoggoXD;
import cards.revenant.Grovewort;
import cards.revenant.ImpatientBall;
import cards.revenant.Invoke;
import cards.revenant.LightningRam;
import cards.revenant.MimicTear;
import cards.revenant.PartingFlame;
import cards.revenant.PhantomSlash;
import cards.revenant.PowerWithin;
import cards.revenant.PumpkinHelm;
import cards.revenant.RedHairIcon;
import cards.revenant.RepeatingCrossbow;
import cards.revenant.Seance;
import cards.revenant.SummonAid;
import cards.revenant.Strike_Revenant;
import cards.revenant.Undeath;
import cards.scholar.AntspurRapier;
import cards.scholar.Defend_Scholar;
import cards.scholar.Homunculus;
import cards.scholar.PenglaiWorship;
import cards.scholar.AjaRedStone;
import cards.scholar.PotionCoveredAshes;
import cards.scholar.RoyalLegacy;
import cards.scholar.Strike_Scholar;
import cards.scholar.Tyranny;
import cards.status.MagicEmber;
import cards.tempcards.CraftmanCreation;
import cards.tempcards.CrucibleToken;
import cards.tempcards.FadingPrimalGlintstone;
import cards.tempcards.PhantomDoggo;
import cards.tempcards.PhantomFrederick;
import cards.tempcards.PhantomHelen;
import cards.tempcards.PhantomAsimi;
import cards.tempcards.PhantomSebastian;
import cards.wylder.BlueWhiteWoodenShield;
import cards.wylder.Barehanded;
import cards.wylder.BloodVeil;
import cards.wylder.BloodhoundFinesse;
import cards.wylder.BloodhoundStep;
import cards.wylder.CeruleanDagger;
import cards.wylder.ManikinShield;
import cards.wylder.NoxFlowingShieldTypeI;
import cards.wylder.NoxFlowingShieldTypeP;
import cards.wylder.OnslaughtStake;
import cards.wylder.Onikiri;
import cards.wylder.PitaBread;
import cards.wylder.PowerOfNight;
import cards.wylder.RemoveArmor;
import cards.undertaker.AlbinauricProof;
import cards.undertaker.ChimeOfWant;
import cards.undertaker.DeadAgain;
import cards.undertaker.Denial;
import cards.undertaker.Delirium;
import cards.undertaker.Defend_Undertaker;
import cards.undertaker.EyesWithin;
import cards.undertaker.ForbiddenSun;
import cards.undertaker.GreatSwampTome;
import cards.undertaker.NormalHammer;
import cards.undertaker.OuterBoon;
import cards.undertaker.SoulAppease;
import cards.undertaker.Strike_Undertaker;
import cards.undertaker.WhatsUp;
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
import cards.wylder.WolfGreatshield;
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
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import general.BulkPotionQueue;
import general.DeadEnemyStats;
import general.EnemyBattleStartSnapshot;
import general.GuidanceStats;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import powers.BellowingDragoncrestPower;
import powers.BlendingPower;
import powers.LingeringDragoncrestPower;
import powers.PaintWorldPower;
import powers.RedHairIconPower;
import powers.SlumberingDragoncrestPower;
import potions.CrimsonPotion;
import potions.DredgeMossClump;
import potions.DuskHerbDew;
import potions.HaligtreeBalm;
import potions.IntelligencePotion;
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
import relics.KleinBottle;
import relics.LooksLens;
import relics.MagicCocktail;
import relics.EagleEye;
import relics.NightShard;
import relics.DeepWoodBranch;
import relics.MagnificentPoise;
import relics.OldGreataxe;
import relics.PetrifiedDragonBone;
import relics.PrescientGlintEye;
import relics.CalamityWard;
import relics.RitualBand;
import relics.ScrollOfLogan;
import relics.SacredTimber;
import relics.SeedbedCurse;
import relics.SoulForge;
import relics.SummonSpirit;
import relics.ToastyMittens;
import relics.Trance;
import relics.WhisperingEarring;
import relics.WheelCatalyst;
import relics.YearsEntwined;
import variables.CrucibleTokenBloodburnVariable;
import variables.CrucibleTokenPoisonVariable;
import variables.CrucibleTokenScarletRotVariable;
import variables.ScheduledVariable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

@SpireInitializer
public class ERNMod implements RelicGetSubscriber, PostPowerApplySubscriber, PostExhaustSubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber, EditCharactersSubscriber,
        PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber,
        OnCardUseSubscriber, EditKeywordsSubscriber, OnPowersModifiedSubscriber, PostDrawSubscriber,
        PostEnergyRechargeSubscriber, OnStartBattleSubscriber {
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
        BaseMod.addDynamicVariable(new CrucibleTokenPoisonVariable());
        BaseMod.addDynamicVariable(new CrucibleTokenScarletRotVariable());
        BaseMod.addDynamicVariable(new CrucibleTokenBloodburnVariable());
        BaseMod.addDynamicVariable(new ScheduledVariable());
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
        BaseMod.addPotion(IntelligencePotion.class, RECLUSE_COLOR.cpy(), RECLUSE_COLOR.cpy(), Color.WHITE.cpy(),
                IntelligencePotion.POTION_ID, ERNModClassEnum.Recluse_CLASS);
        BaseMod.addPotion(CrimsonPotion.class, RECLUSE_COLOR.cpy(),
                new Color(0.55F, 0.08F, 0.12F, 1.0F), Color.WHITE.cpy(),
                CrimsonPotion.POTION_ID, ERNModClassEnum.Recluse_CLASS);
        BaseMod.addPotion(DuskHerbDew.class, RECLUSE_COLOR.cpy(),
                new Color(0.35F, 0.22F, 0.42F, 1.0F), Color.WHITE.cpy(),
                DuskHerbDew.POTION_ID, ERNModClassEnum.Recluse_CLASS);
        BaseMod.addPotion(DredgeMossClump.class, RAIDER_COLOR.cpy(), RAIDER_COLOR.cpy(), Color.WHITE.cpy(),
                DredgeMossClump.POTION_ID, ERNModClassEnum.Raider_CLASS);
        BaseMod.addPotion(HaligtreeBalm.class, SCHOLAR_COLOR.cpy(),
                new Color(0.82F, 0.78F, 0.56F, 1.0F), Color.WHITE.cpy(),
                HaligtreeBalm.POTION_ID, ERNModClassEnum.Scholar_CLASS);
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
        String ui;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            card = "localization/ERNMod_cards-zh.json";
            relic = "localization/ERNMod_relics-zh.json";
            power = "localization/ERNMod_powers-zh.json";
            potion = "localization/ERNMod_potions-zh.json";
            ui = "localization/ERNMod_ui-zh.json";
        } else {
            card = "localization/ERNMod_cards-eng.json";
            relic = "localization/ERNMod_relics-eng.json";
            power = "localization/ERNMod_powers-eng.json";
            potion = "localization/ERNMod_potions-eng.json";
            ui = "localization/ERNMod_ui-eng.json";
        }

        String relicStrings = Gdx.files.internal(relic).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String cardStrings = Gdx.files.internal(card).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal(power).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String potionStrings = Gdx.files.internal(potion).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String uiStrings = Gdx.files.internal(ui).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
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
        this.cardsToAdd.add(new BloodhoundFinesse());
        this.cardsToAdd.add(new BloodhoundStep());
        this.cardsToAdd.add(new CrowQuills());
        this.cardsToAdd.add(new SymbolOfAvarice());
        this.cardsToAdd.add(new Ubadachi());
        this.cardsToAdd.add(new Onikiri());
        this.cardsToAdd.add(new ManikinShield());
        this.cardsToAdd.add(new CeruleanDagger());
        this.cardsToAdd.add(new WolfGreatshield());
        this.cardsToAdd.add(new PowerOfNight());
        this.cardsToAdd.add(new Strike_Guardian());
        this.cardsToAdd.add(new Defend_Guardian());
        this.cardsToAdd.add(new cards.guardian.Whirlwind());
        this.cardsToAdd.add(new WarmingStone());
        this.cardsToAdd.add(new WingsOfSalvation());
        this.cardsToAdd.add(new ChargeForth());
        this.cardsToAdd.add(new Bonewheel());
        this.cardsToAdd.add(new SpikedShield());
        this.cardsToAdd.add(new BarricadeShield());
        this.cardsToAdd.add(new Strike_Ironeye());
        this.cardsToAdd.add(new Defend_Ironeye());
        this.cardsToAdd.add(new CobCannon());
        this.cardsToAdd.add(new Imitater());
        this.cardsToAdd.add(new StringSecret());
        this.cardsToAdd.add(new SmoothSilkyStone());
        this.cardsToAdd.add(new PetrifiedSomething());
        this.cardsToAdd.add(new CeruleanwhorlBubble());
        this.cardsToAdd.add(new FellowshipAtt());
        this.cardsToAdd.add(new Strike_Raider());
        this.cardsToAdd.add(new Defend_Raider());
        this.cardsToAdd.add(new CraftmanHammer());
        this.cardsToAdd.add(new BloodWall());
        this.cardsToAdd.add(new BloodDebt());
        this.cardsToAdd.add(new BloodTax());
        this.cardsToAdd.add(new BleedStone());
        this.cardsToAdd.add(new Boltstone());
        this.cardsToAdd.add(new DarknightStone());
        this.cardsToAdd.add(new Faintstone());
        this.cardsToAdd.add(new FiredrakeStone());
        this.cardsToAdd.add(new Kick());
        this.cardsToAdd.add(new Pickaxe());
        this.cardsToAdd.add(new RawStone());
        this.cardsToAdd.add(new TitaniteShard());
        this.cardsToAdd.add(new TitaniteChunk());
        this.cardsToAdd.add(new TitaniteSlab());
        this.cardsToAdd.add(new TwinklingTitanite());
        this.cardsToAdd.add(new DemonTitanite());
        this.cardsToAdd.add(new DragonScale());
        this.cardsToAdd.add(new BloodfiendArm());
        this.cardsToAdd.add(new MagicStone());
        this.cardsToAdd.add(new OldMundaneStone());
        this.cardsToAdd.add(new Palestone());
        this.cardsToAdd.add(new PoisonStone());
        this.cardsToAdd.add(new PrismStone());
        this.cardsToAdd.add(new DuelistGreataxe());
        this.cardsToAdd.add(new Strike_Duchess());
        this.cardsToAdd.add(new Defend_Duchess());
        this.cardsToAdd.add(new DynastFinesse());
        this.cardsToAdd.add(new cards.duchess.Restage());
        this.cardsToAdd.add(new WaterfowlDance());
        this.cardsToAdd.add(new Mortgage());
        this.cardsToAdd.add(new SellswordTwinblades());
        this.cardsToAdd.add(new CeruleanburstCrystal());
        this.cardsToAdd.add(new WildStrikes());
        this.cardsToAdd.add(new GoldTracer());
        this.cardsToAdd.add(new DarkSilverTracer());
        this.cardsToAdd.add(new ChillingMist());
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
        this.cardsToAdd.add(new RiversOfBlood());
        this.cardsToAdd.add(new StTrinaSword());
        this.cardsToAdd.add(new HoarfrostStomp());
        this.cardsToAdd.add(new FrenziedBurst());
        this.cardsToAdd.add(new UnendurableFrenzy());
        this.cardsToAdd.add(new BloodboonRitual());
        this.cardsToAdd.add(new BloodhoundFang());
        this.cardsToAdd.add(new Seppuku());
        this.cardsToAdd.add(new PowerOfErdtree());
        this.cardsToAdd.add(new AriandelBlood());
        this.cardsToAdd.add(new Strike_Revenant());
        this.cardsToAdd.add(new Defend_Revenant());
        this.cardsToAdd.add(new PhantomSlash());
        this.cardsToAdd.add(new Invoke());
        this.cardsToAdd.add(new Dirge());
        this.cardsToAdd.add(new Grovewort());
        this.cardsToAdd.add(new PumpkinHelm());
        this.cardsToAdd.add(new RepeatingCrossbow());
        this.cardsToAdd.add(new BlackButler());
        this.cardsToAdd.add(new LightningRam());
        this.cardsToAdd.add(new Seance());
        this.cardsToAdd.add(new Undeath());
        this.cardsToAdd.add(new PowerWithin());
        this.cardsToAdd.add(new PartingFlame());
        this.cardsToAdd.add(new SummonAid());
        this.cardsToAdd.add(new RedHairIcon());
        this.cardsToAdd.add(new ImpatientBall());
        this.cardsToAdd.add(new DoggoXD());
        this.cardsToAdd.add(new MimicTear());
        this.cardsToAdd.add(new Strike_Scholar());
        this.cardsToAdd.add(new Defend_Scholar());
        this.cardsToAdd.add(new PotionCoveredAshes());
        this.cardsToAdd.add(new AntspurRapier());
        this.cardsToAdd.add(new Tyranny());
        this.cardsToAdd.add(new PenglaiWorship());
        this.cardsToAdd.add(new RoyalLegacy());
        this.cardsToAdd.add(new Homunculus());
        this.cardsToAdd.add(new AjaRedStone());
        this.cardsToAdd.add(new Strike_Undertaker());
        this.cardsToAdd.add(new Defend_Undertaker());
        this.cardsToAdd.add(new Denial());
        this.cardsToAdd.add(new Delirium());
        this.cardsToAdd.add(new OuterBoon());
        this.cardsToAdd.add(new EyesWithin());
        this.cardsToAdd.add(new GreatSwampTome());
        this.cardsToAdd.add(new ForbiddenSun());
        this.cardsToAdd.add(new Zealotry());
        this.cardsToAdd.add(new AlbinauricProof());
        this.cardsToAdd.add(new WhatsUp());
        this.cardsToAdd.add(new ChimeOfWant());
        this.cardsToAdd.add(new SoulAppease());
        this.cardsToAdd.add(new NormalHammer());
        this.cardsToAdd.add(new DeadAgain());
        this.cardsToAdd.add(new GlitstonePebble());
        this.cardsToAdd.add(new FallControl());
        this.cardsToAdd.add(new CannonOfHaima());
        this.cardsToAdd.add(new RockBlaster());
        this.cardsToAdd.add(new FarronFlashsword());
        this.cardsToAdd.add(new StarcallerCry());
        this.cardsToAdd.add(new AmbushShard());
        this.cardsToAdd.add(new ArchiveOccult());
        this.cardsToAdd.add(new GlintstoneCrown());
        this.cardsToAdd.add(new ResonantSoul());
        this.cardsToAdd.add(new SoulFlash());
        this.cardsToAdd.add(new SoulBolt());
        this.cardsToAdd.add(new SoulGeyser());
        this.cardsToAdd.add(new SoulVortex());
        this.cardsToAdd.add(new GreatResonantSoul());
        this.cardsToAdd.add(new SwiftGlintstoneShard());
        this.cardsToAdd.add(new HiddenBody());
        this.cardsToAdd.add(new HiddenWeapon());
        this.cardsToAdd.add(new TwilightHerb());
        this.cardsToAdd.add(new CarianPiercer());
        this.cardsToAdd.add(new Yearn());
        this.cardsToAdd.add(new Dodge());
        this.cardsToAdd.add(new FistfulOfAsh());
        this.cardsToAdd.add(new MagicKinship());
        this.cardsToAdd.add(new UnleashMagic());
        this.cardsToAdd.add(new StrongMagicShield());
        this.cardsToAdd.add(new Dispelling());
        this.cardsToAdd.add(new StoneClutch());
        this.cardsToAdd.add(new FoundingRainOfStars());
        this.cardsToAdd.add(new DarkOrb());
        this.cardsToAdd.add(new DarkFog());
        this.cardsToAdd.add(new DarkHand());
        this.cardsToAdd.add(new DarkBead());
        this.cardsToAdd.add(new CometAzur());
        this.cardsToAdd.add(new GlintbladePhalanx());
        this.cardsToAdd.add(new GreatSoulDregs());
        this.cardsToAdd.add(new HomingCrystalSoulmass());
        this.cardsToAdd.add(new Crystallization());
        this.cardsToAdd.add(new CrystalScroll());
        this.cardsToAdd.add(new CrystalRingShield());
        this.cardsToAdd.add(new CrystalScrap());
        this.cardsToAdd.add(new GravityCrystal());
        this.cardsToAdd.add(new MendicantStaff());
        this.cardsToAdd.add(new TheGnawling());
        this.cardsToAdd.add(new PowerOfDragonlord());
        this.cardsToAdd.add(new PunishmentBriars());
        this.cardsToAdd.add(new QuellStorm());
        this.cardsToAdd.add(new Quasicrystal());
        this.cardsToAdd.add(new CollectAffinity());
        this.cardsToAdd.add(new CrystalSoulSpear());
        this.cardsToAdd.add(new LapisLazuliGeode());
        this.cardsToAdd.add(new Chameleon());
        this.cardsToAdd.add(new MorionBlade());
        this.cardsToAdd.add(new WhiteLightning());
        this.cardsToAdd.add(new ElementalDefense());
        this.cardsToAdd.add(new SoulbloodSong());
        this.cardsToAdd.add(new LingeringDragoncrest());
        this.cardsToAdd.add(new BellowingDragoncrest());
        this.cardsToAdd.add(new Bloodbite());
        this.cardsToAdd.add(new BloodSigil());
        this.cardsToAdd.add(new SlumberingDragoncrest());
        this.cardsToAdd.add(new SinBriars());
        this.cardsToAdd.add(new Eagle());
        this.cardsToAdd.add(new PrimalGlintstone());
        this.cardsToAdd.add(new SoulVessel());
        this.cardsToAdd.add(new Ictarus());
        this.cardsToAdd.add(new MagicRecycle());
        this.cardsToAdd.add(new SeekGuidance());
        this.cardsToAdd.add(new ReignitedCinder());
        this.cardsToAdd.add(new Replication());
        this.cardsToAdd.add(new LawOfRegression());
        this.cardsToAdd.add(new FadingPrimalGlintstone());
        this.cardsToAdd.add(new PhantomHelen());
        this.cardsToAdd.add(new PhantomFrederick());
        this.cardsToAdd.add(new PhantomSebastian());
        this.cardsToAdd.add(new PhantomDoggo());
        this.cardsToAdd.add(new PhantomAsimi());
        this.cardsToAdd.add(new CrucibleToken());
        this.cardsToAdd.add(new CraftmanCreation());
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
        addERNRelic(new DeepWoodBranch(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new CinderellaRosette(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new RitualBand(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new WheelCatalyst(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new NightShard(), AbstractCardEnum.Recluse_COLOR);
        addERNRelic(new relics.SixthSense(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new ToastyMittens(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new JosephForesight(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new HonorOfPinionfolk(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new EagleEye(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new FighterResolve(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new OldGreataxe(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new PetrifiedDragonBone(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new BorrowedLife(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new DebtToBond(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new FighterDestined(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new MagnificentPoise(), AbstractCardEnum.Duchess_COLOR);
        addERNRelic(new CalamityWard(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SeedbedCurse(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new CrucibleCodex(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SummonSpirit(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SoulForge(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new YearsEntwined(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SacredTimber(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new Bagcraft(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new LooksLens(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new KleinBottle(), AbstractCardEnum.Scholar_COLOR);
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
        GuidanceStats.startPlayerTurn();
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
        EnemyBattleStartSnapshot.reset();
        GuidanceStats.resetCombat();
        DeadEnemyStats.resetCombat();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        GuidanceStats.resetCombat();
        DeadEnemyStats.resetCombat();
    }

    @Override
    public void receivePostDraw(AbstractCard arg0) {}

    @Override
    public void receiveEditKeywords() {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            BaseMod.addKeyword("重放", new String[] { "重放" },
                    "打出时，额外打出指定次数。由 #y重放 打出的牌不会再次触发 #y重放 。");
            BaseMod.addKeyword("锻造", new String[] { "锻造" },
                    "使用素材牌对非素材牌 #y质变 或 #y强化 。");
            BaseMod.addKeyword("质变", new String[] { "质变" },
                    "质变素材包括： #y粗刚石 ， #y苍光石 ， #y火龙石 ， #y雷光石 ， #y暗夜石 ， #y毒块石 ， #y血块石 ， #y魔力石 ， #y暗澹古石 ， #y白石 。");
            BaseMod.addKeyword("强化", new String[] { "强化" },
                    "强化素材包括： #y楔形石碎片 ， #y楔形石块 ， #y楔形石原盘 ， #y光辉楔形石 ， #y恶魔楔形石 ， #y龙鳞 。");
            BaseMod.addKeyword("感应", new String[] { "感应" },
                    "通过潜在能力，能比较容易找到罕见牌。");
            BaseMod.addKeyword("荆棘", new String[] { "荆棘" },
                    "受到攻击时，对攻击者造成等同于层数的伤害。");
            BaseMod.addKeyword("召唤", new String[] { "召唤" },
                    "召唤包括： #y召唤：海伦 ， #y召唤：弗雷德利克 ， #y召唤：塞巴斯蒂安 ， #y召唤：大狗 ， #y召唤：安诗糸 。");
            BaseMod.addKeyword("基础召唤", new String[] { "基础召唤" },
                    "基础召唤包括： #y召唤：海伦 ， #y召唤：弗雷德利克 ， #y召唤：塞巴斯蒂安 。");
            BaseMod.addKeyword("召唤：大狗", new String[] { "召唤：大狗" },
                    "获得 #b2 点格挡，将1张 #y幻影大狗 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：安诗糸", new String[] { "召唤：安诗糸" },
                    "获得 #b5 点格挡，将1张 #y幻影安诗糸 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：阿诗糸", new String[] { "召唤：阿诗糸" },
                    "获得 #b5 点格挡，将1张 #y幻影安诗糸 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("共击", new String[] { "共击" },
                    "只有当对应 #y召唤 时可以打出。");
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
            BaseMod.addKeyword("智力", new String[] { "智力" },
                    "魔法伤害和魔法 格挡 获得等同于智力层数的额外数值。");
            BaseMod.addKeyword("魔法", new String[] { "魔法" },
                    "效果中有“魔法”的卡牌。");
            BaseMod.addKeyword("结晶", new String[] { "结晶" },
                    "结晶包括： #y灵魂结晶枪 ， #y结晶卷轴 ， #y结晶环盾 ， #y追踪灵魂结晶块 ， #y崩裂结晶 ， #y重力结晶 。");
            BaseMod.addKeyword("散装", new String[] { "散装" },
                    "在战斗结束时消失。");
            BaseMod.addKeyword("展翼", new String[] { "展翼" },
                    "将所受到的下几次伤害降低50%。每当你受到未被格挡的攻击伤害时，失去1层。");
            BaseMod.addKeyword("振翼", new String[] { "振翼" },
                    "将所受到的下几次伤害降低50%。每当你受到攻击伤害时，失去1层。");
            BaseMod.addKeyword("完美格挡", new String[] { "完美格挡" },
                    "持续若干回合。如果敌人攻击结束时你有大于等于 #b15 点 #y格挡 ，获得 #b6 点 #y活力 。");
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
            BaseMod.addKeyword("灵力", new String[] { "灵力" },
                    "幻影牌的所有卡面数字都增加等同于 #y灵力 的数值。");
            BaseMod.addKeyword("信仰", new String[] { "信仰" },
                    "奇术伤害和奇术 格挡 获得或失去额外数值。");
            BaseMod.addKeyword("奇术", new String[] { "奇术" },
                    "效果中有“奇术”的卡牌。");
            BaseMod.addKeyword("冻伤", new String[] { "冻伤" },
                    "每有1层 #y冻伤 ，从 #y攻击 受到的伤害增加 #b20% 。回合开始时，将 #y冻伤 层数减少 #b1 。");
            BaseMod.addKeyword("睡眠", new String[] { "睡眠" },
                    "每有1层 #y睡眠 ，相当于失去 #b1 点 #y力量 。回合结束时，将 #y睡眠 层数减少 #b1 。");
            BaseMod.addKeyword("发狂", new String[] { "发狂" },
                    "获得 #b10 层 #y发狂 时，受到 #b10% 最大生命值的伤害。如果是敌人，则额外在当前回合 #y击晕 。");
            BaseMod.addKeyword("出血", new String[] { "出血" },
                    "获得 #b10 层 #y出血 时，受到 #b30% 当前生命值的伤害。如果是敌人，则额外失去 #b30% 当前生命值的最大生命值。");
            BaseMod.addKeyword("削韧", new String[] { "削韧" },
                    "获得 #b10 层 #y削韧 时，敌人在当前回合 #y击晕 ，玩家结束当前回合。");
            BaseMod.addKeyword("保留卡牌", new String[] { "保留卡牌" },
                    "在你的回合结束时，保留若干张牌。");
            BaseMod.addKeyword("金属化", new String[] { "金属化" },
                    "在你的回合结束时，获得 #y格挡 。");
            BaseMod.addKeyword("异常", new String[] { "异常" },
                    " #y中毒 ， #y猩红腐败 ， #y燃命 。");
            BaseMod.addKeyword("异变", new String[] { "异变" },
                    " #y冻伤 ， #y睡眠 ， #y发狂 ， #y出血 。");
        } else {
            BaseMod.addKeyword("Replay", new String[] { "replay" },
                    "When played, play this card an additional number of times. Cards played by #yReplay do not trigger #yReplay again.");
            BaseMod.addKeyword("Smithing", new String[] { "smithing" },
                    "Use material cards to #yInfuse or #yReinforce non-material cards.");
            BaseMod.addKeyword("Infusion", new String[] { "infusion" },
                    "Infusion materials include: #yRaw #yStone , #yFaintstone , #yFiredrake #yStone , #yBoltstone , #yDarknight #yStone , #yPoison #yStone , #yBleed #yStone , #yMagic #yStone , #yOld #yMundane #yStone , and #yPalestone .");
            BaseMod.addKeyword("Reinforcement", new String[] { "reinforcement" },
                    "Reinforcement materials include: #yTitanite #yShard , #yTitanite #yChunk , #yTitanite #ySlab , #yTwinkling #yTitanite , #yDemon #yTitanite , and #yDragon #yScale .");
            BaseMod.addKeyword("Arcane", new String[] { "arcane" },
                    "Dormant power helps discover Rare cards.");
            BaseMod.addKeyword("Thorns", new String[] { "thorns" },
                    "When attacked, deal damage to the attacker equal to this amount.");
            BaseMod.addKeyword("Summon", new String[] { "summon" },
                    "Summons include: #ySummon: #yHelen , #ySummon: #yFrederick , #ySummon: #ySebastian , #ySummon: #yDoggo , and #ySummon: #yAsimi .");
            BaseMod.addKeyword("Basic Summon", new String[] { "basic summon" },
                    "Basic Summons include: #ySummon: #yHelen , #ySummon: #yFrederick , and #ySummon: #ySebastian .");
            BaseMod.addKeyword("Summon: Helen", new String[] { "summon: helen" },
                    "Gain #b3 Block, add 1 #yPhantom #yHelen to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Frederick", new String[] { "summon: frederick" },
                    "Gain #b8 Block, add 1 #yPhantom #yFrederick to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Sebastian", new String[] { "summon: sebastian" },
                    "Gain #b5 Block, add 1 #yPhantom #ySebastian to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Doggo", new String[] { "summon: doggo" },
                    "Gain #b2 Block, add 1 #yPhantom #yDoggo to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Asimi", new String[] { "summon: asimi" },
                    "Gain #b5 Block, add 1 #yPhantom #yAsimi to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Pumpkin Helm", new String[] { "pumpkin helm" },
                    "If you have #ySummon: #yFrederick, #yBlock is not removed at the start of your turn.");
            BaseMod.addKeyword("Repeating Crossbow", new String[] { "repeating crossbow" },
                    "If you have #ySummon: #yHelen, played #yPhantom #yHelen is played #b2 additional times.");
            BaseMod.addKeyword("Black Butler", new String[] { "black butler" },
                    "If you have #ySummon: #ySebastian, #yRetain your hand this turn.");
            BaseMod.addKeyword("Slash", new String[] { "slash" },
                    "Can only be played while you have the corresponding #ySummon.");
            BaseMod.addKeyword("Intelligence", new String[] { "intelligence" },
                    "Magic damage and Magic Block gain additional value equal to your Intelligence.");
            BaseMod.addKeyword("Magic", new String[] { "magic" },
                    "Cards whose effects contain \"Magic\".");
            BaseMod.addKeyword("Crystal", new String[] { "crystal" },
                    "Crystals include: #yCrystal #ySoul #ySpear , #yCrystal #yScroll , #yCrystal #yRing #yShield , #yHoming #yCrystal #ySoulmass , #yCrystal #yScrap , and #yGravity #yCrystal .");
            BaseMod.addKeyword("Bulk", new String[] { "bulk" },
                    "Vanishes at the end of combat.");
            BaseMod.addKeyword("Soar", new String[] { "soar" },
                    "Reduces the next few instances of damage taken by 50%. Lose 1 stack whenever you take unblocked attack damage.");
            BaseMod.addKeyword("Hover", new String[] { "hover" },
                    "Reduces the next few instances of damage taken by 50%. Lose 1 stack whenever you take attack damage.");
            BaseMod.addKeyword("Perfect Guard", new String[] { "perfect guard" },
                    "Lasts for several turns. If you have at least #b15 #yBlock when enemy attacks finish, gain #b6 #yVigor.");
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
                    "Thaumaturgy damage and Thaumaturgy Block gain or lose additional value.");
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
        GuidanceStats.recordCardUse(abstractCard);
        if (BedOfMagic.ID.equals(abstractCard.cardID)) {
            return;
        }
        BedOfMagic.recordPlayedCard(abstractCard);
    }

    @Override
    public void receivePostBattle(AbstractRoom r) {
        BedOfMagic.resetCombat();
        EnemyBattleStartSnapshot.reset();
        DeadEnemyStats.resetCombat();
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
