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
import cards.duchess.BewitchingBranch;
import cards.duchess.AssassinGambit;
import cards.duchess.Poleblade;
import cards.duchess.BladeOfCalling;
import cards.duchess.CeruleanburstCrystal;
import cards.duchess.CrystalEnchanted;
import cards.duchess.ChillingMist;
import cards.duchess.LineOfStars;
import cards.duchess.ConvenientBundlingStrap;
import cards.duchess.DancerEnchantedSwords;
import cards.duchess.DancingBlade;
import cards.duchess.Euporia;
import cards.duchess.DarkWoodGrain;
import cards.duchess.Defend_Duchess;
import cards.duchess.DarkSilverTracer;
import cards.duchess.Darkdrift;
import cards.duchess.DynastFinesse;
import cards.duchess.EventualGreatness;
import cards.duchess.FlowingTechniques;
import cards.duchess.GoldTracer;
import cards.duchess.GodslayerGreatsword;
import cards.duchess.HornetRing;
import cards.duchess.IceRapier;
import cards.duchess.Misericorde;
import cards.duchess.Bouquet;
import cards.duchess.RuinousGhostflame;
import cards.duchess.BloodflameBlade;
import cards.duchess.ShadowGuard;
import cards.duchess.RaptorOfTheMists;
import cards.duchess.BlackKnife;
import cards.duchess.JupiterHeart;
import cards.duchess.PowerOfTheGreaterWill;
import cards.duchess.Mortgage;
import cards.duchess.MushroomCrown;
import cards.duchess.InvertedStatue;
import cards.duchess.NoMoney;
import cards.duchess.SantierSpear;
import cards.duchess.ScavengerCurvedSword;
import cards.duchess.BlindSpot;
import cards.duchess.SellswordTwinblades;
import cards.duchess.SlimeMold;
import cards.duchess.StormCurvedSword;
import cards.duchess.Strike_Duchess;
import cards.duchess.SwiftSlash;
import cards.duchess.Urumi;
import cards.duchess.HaloScythe;
import cards.duchess.WaterfowlDance;
import cards.duchess.WindyCrystal;
import cards.duchess.WildStrikes;
import cards.duchess.WhiteLightCharge;
import cards.executor.Blending;
import cards.executor.AriandelBlood;
import cards.executor.AspectsOfTheCrucibleBeast;
import cards.executor.BlackFlame;
import cards.executor.Darkstorm;
import cards.executor.VenomousFang;
import cards.executor.RedRustScimitar;
import cards.executor.BlackFlameBlade;
import cards.executor.BlackFlameRitual;
import cards.executor.BlackFlameBarrier;
import cards.executor.BlackFlameTornado;
import cards.executor.BloodboonRitual;
import cards.executor.BloodhoundFang;
import cards.executor.ChaosBlade;
import cards.executor.InseparableSword;
import cards.executor.RagingBeast;
import cards.executor.Moonveil;
import cards.executor.Gnaw;
import cards.executor.DragonTooth;
import cards.executor.DeathFlare;
import cards.executor.SwordOfMilos;
import cards.executor.SpinningGuillotine;
import cards.executor.GiantHunt;
import cards.executor.AlabasterLordsPull;
import cards.executor.OnyxLordsRepulsion;
import cards.executor.SolderingIron;
import cards.executor.Shackle;
import cards.executor.CrossInfection;
import cards.executor.CrucibleCodex;
import cards.executor.DarkBlood;
import cards.executor.DeadlyDance;
import cards.executor.HolyGround;
import cards.executor.FingerprintSpear;
import cards.executor.WavesOfDarkness;
import cards.executor.CalamitySpraymist;
import cards.executor.DeathPoker;
import cards.executor.DestinedDeath;
import cards.executor.Defend_Executor;
import cards.executor.EmitForce;
import cards.executor.HeartWall;
import cards.executor.Expose;
import cards.executor.FreezingMist;
import cards.executor.RottenBreath;
import cards.executor.PosionMothFlight;
import cards.executor.SnapWillow;
import cards.executor.NoFlyZone;
import cards.executor.CalamityExultation;
import cards.executor.FrenzyflameThrust;
import cards.executor.CladInBlackflame;
import cards.executor.ScorpionStinger;
import cards.executor.Appeasement;
import cards.executor.BlackFlamestoneParma;
import cards.executor.Bully;
import cards.executor.PosionedRelief;
import cards.executor.FrenziedBurst;
import cards.executor.GammaKnife;
import cards.executor.HoarfrostStomp;
import cards.executor.InfectionStrike;
import cards.executor.LetFeastBegin;
import cards.executor.Metastasis;
import cards.executor.Numbness;
import cards.executor.OrderBlade;
import cards.executor.PaintWorld;
import cards.executor.PowerOfErdtree;
import cards.executor.RiversOfBlood;
import cards.executor.ScarletAeonia;
import cards.executor.ViperBite;
import cards.executor.UnalloyedGoldNeedle;
import cards.executor.Seppuku;
import cards.executor.SerpentHunter;
import cards.executor.NomadicFrenzyflame;
import cards.executor.InescapableFrenzy;
import general.PlayerDebuffStats;
import cards.executor.StTrinaSword;
import cards.executor.Strike_Executor;
import cards.executor.UnendurableFrenzy;
import cards.guardian.BlackKnightHalberd;
import cards.guardian.NeedlePiercer;
import cards.guardian.SpinningGravityThrust;
import cards.guardian.Bonewheel;
import cards.guardian.BarricadeShield;
import cards.guardian.Brightbug;
import cards.guardian.CallLightning;
import cards.guardian.ChargeForth;
import cards.guardian.Cyclone;
import cards.guardian.DuelingShield;
import cards.guardian.Defend_Guardian;
import cards.guardian.FeatherFan;
import cards.guardian.ForcedLanding;
import cards.guardian.CrucifixOfTheMadKing;
import cards.guardian.GoldenHalberd;
import cards.guardian.Grandeur;
import cards.guardian.Assault;
import cards.guardian.Retaliatory;
import cards.guardian.GolemHalberd;
import cards.guardian.BastardSword;
import cards.guardian.SpearcallRitual;
import cards.guardian.GlintstoneKris;
import cards.guardian.GowerRingOfProtection;
import cards.guardian.HauntingWithin;
import cards.guardian.LessLikelyToBeTargeted;
import cards.guardian.Ridicule;
import cards.guardian.KineticBombardment;
import cards.guardian.MantleOfThorns;
import cards.guardian.ShroudingHeavens;
import cards.guardian.Sovereignty;
import cards.guardian.FadeIntoShadow;
import cards.guardian.GiantDoorShield;
import cards.guardian.Preening;
import cards.guardian.PurpleSign;
import cards.guardian.SpikedShield;
import cards.guardian.SplitleafGreatsword;
import cards.guardian.Stormcaller;
import cards.guardian.Strike_Guardian;
import cards.guardian.SunlightStraightSword;
import cards.guardian.TheBirds;
import cards.guardian.WarmingStone;
import cards.guardian.WingedKnightHalberd;
import cards.guardian.WingStance;
import cards.guardian.MillwoodKnightArmor;
import cards.guardian.Typhoon;
import cards.guardian.SunlightShield;
import cards.guardian.GreatshieldOfGlory;
import cards.guardian.FalconShield;
import cards.guardian.Prolong;
import cards.guardian.Aufheben;
import cards.guardian.FatalAppetite;
import cards.guardian.StormWing;
import cards.guardian.BeastRepellentTorch;
import cards.guardian.CrescentMoonAxe;
import cards.guardian.FallingstarBeastJaw;
import cards.guardian.WingsOfSalvation;
import cards.ironeye.BlackClawAssistant;
import cards.ironeye.Barter;
import cards.ironeye.BillPlease;
import cards.ironeye.BitComet;
import cards.ironeye.Marking;
import cards.ironeye.PoisonMarking;
import cards.ironeye.Bounty;
import cards.ironeye.Avelyn;
import cards.ironeye.ShotClub;
import cards.ironeye.CeruleanwhorlBubble;
import cards.ironeye.DeadOrAlive;
import cards.ironeye.Defend_Ironeye;
import cards.ironeye.DivineSpear;
import cards.ironeye.Entropy;
import cards.ironeye.Fluctuation;
import cards.ironeye.FellowshipAtt;
import cards.ironeye.CobCannon;
import cards.ironeye.FragrantBranchOfYore;
import cards.ironeye.GhostBlade;
import cards.ironeye.GhostMillstone;
import cards.ironeye.GoldenCrux;
import cards.ironeye.SleepEvermore;
import cards.ironeye.NineStagesOfDecay;
import cards.ironeye.GoughGreatbow;
import cards.ironeye.YoungWhiteBranch;
import cards.ironeye.GoldPickledFowlFoot;
import cards.ironeye.HeatUp;
import cards.ironeye.Imitater;
import cards.ironeye.Lifegem;
import cards.ironeye.Leverage;
import cards.ironeye.LoyaltyCard;
import cards.ironeye.MidasTouch;
import cards.ironeye.OldRadiantLifegem;
import cards.ironeye.PetrifiedSomething;
import cards.ironeye.PharrosLockstone;
import cards.ironeye.Plunder;
import cards.ironeye.PlatinumCard;
import cards.ironeye.PortableShop;
import cards.ironeye.RainOfArrows;
import cards.ironeye.RustedCoin;
import cards.ironeye.RustedGoldCoin;
import cards.ironeye.ShieldOfWant;
import cards.ironeye.SilverPickledFowlFoot;
import cards.ironeye.SmoothSilkyStone;
import cards.ironeye.SnakeSlough;
import cards.ironeye.StringSecret;
import cards.ironeye.Strike_Ironeye;
import cards.ironeye.Usury;
import cards.ironeye.VendingMachine;
import cards.raider.Defend_Raider;
import cards.raider.AcidSurge;
import cards.raider.AssemblyLine;
import cards.raider.BloodDebt;
import cards.raider.BloodTax;
import cards.raider.BloodWall;
import cards.raider.BlessedDew;
import cards.raider.BloodfiendArm;
import cards.raider.BleedStone;
import cards.raider.BoneFist;
import cards.raider.BlueTearstoneRing;
import cards.raider.Boltstone;
import cards.raider.Caestus;
import cards.raider.Club;
import cards.raider.CraftmanHammer;
import cards.raider.CourtesyEnough;
import cards.raider.DarknightStone;
import cards.raider.DemonTitanite;
import cards.raider.DoOrDie;
import cards.raider.DowsingRod;
import cards.raider.TotemStela;
import cards.raider.Dismantle;
import cards.raider.Insurance;
import cards.raider.CatarinaRoundMoon;
import cards.raider.DungPie;
import cards.raider.DullEmber;
import cards.raider.EarthSeeker;
import cards.raider.DragonTorsoStone;
import cards.raider.DragonHeadStone;
import cards.raider.DragonScale;
import cards.raider.Faintstone;
import cards.raider.FiredrakeStone;
import cards.raider.FightThrough;
import cards.raider.FourProngedPlow;
import cards.raider.ForkedHatchet;
import cards.raider.StormRuler;
import cards.raider.Stagger;
import cards.raider.GiantCrusher;
import cards.raider.VordtGreatHammer;
import cards.raider.GreatClub;
import cards.raider.HammerTime;
import cards.raider.HandAxe;
import cards.raider.MagicStone;
import cards.raider.MoonOfNokstella;
import cards.raider.MurkyHandScythe;
import cards.raider.OldMundaneStone;
import cards.raider.Palestone;
import cards.raider.Boomerang;
import cards.undertaker.Pickaxe;
import cards.undertaker.DemonGreatHammer;
import cards.raider.BloodforgedSword;
import cards.raider.PrelateCharge;
import cards.raider.PierceShield;
import cards.raider.Kick;
import cards.raider.StormKick;
import cards.raider.LargeClub;
import cards.raider.Masterwork;
import cards.raider.PoisonStone;
import cards.raider.PowerOfHouseMarais;
import cards.raider.PrismStone;
import cards.raider.RawStone;
import cards.raider.RedTearstoneRing;
import cards.raider.Siegbrau;
import cards.raider.SmithingArtSpears;
import cards.raider.SpinningSlash;
import cards.raider.StoneRing;
import cards.raider.TitaniteChunk;
import cards.raider.TitaniteSlab;
import cards.raider.TitaniteShard;
import cards.raider.TwinklingTitanite;
import cards.raider.DuelistFurledFinger;
import cards.raider.Smithbox;
import cards.raider.DuelistGreataxe;
import cards.raider.ExpectAFight;
import cards.raider.Strike_Raider;
import cards.raider.WingedKnightTwinaxes;
import cards.raider.WarMateriel;
import cards.revenant.BlackButler;
import cards.revenant.BeastClaw;
import cards.revenant.ScaleBearingMerchant;
import cards.revenant.PowerfulWeapon;
import cards.revenant.WatcherStick;
import cards.revenant.ResistanceToAilments;
import cards.revenant.LotOfRunes;
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
import cards.recluse.WitchingHour;
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
import cards.revenant.CursedClaws;
import cards.revenant.Climax;
import cards.revenant.JellyfishShield;
import cards.revenant.PerfectScore;
import cards.revenant.MoonlightGreatsword;
import cards.revenant.SwordOfNightAndFlame;
import cards.revenant.Defend_Revenant;
import cards.revenant.DestitutionAssist;
import cards.revenant.Dirge;
import cards.revenant.DoggoXD;
import cards.revenant.GhostflameIgnition;
import cards.revenant.GhizaWheel;
import cards.revenant.Grovewort;
import cards.revenant.GreatEpee;
import cards.revenant.Immolation;
import cards.revenant.IScream;
import cards.revenant.ThankYouGift;
import cards.revenant.HorizontalAlliance;
import cards.revenant.VerticalAlliance;
import cards.revenant.Confidence;
import cards.revenant.WorkStudy;
import cards.revenant.CoordinatedAttack;
import cards.revenant.WraithCallingBell;
import cards.revenant.ManikinClaws;
import cards.revenant.Conciliation;
import cards.revenant.SharedOrder;
import cards.revenant.ImpatientBall;
import cards.revenant.Initiative;
import cards.revenant.Invoke;
import cards.revenant.LeadingStrike;
import cards.revenant.LadleStrike;
import cards.revenant.Milady;
import cards.revenant.GreatClingingBone;
import cards.revenant.SnailAdvent;
import cards.revenant.Reimbursement;
import cards.revenant.Mischief;
import cards.revenant.LightningRam;
import cards.revenant.MoonStance;
import cards.revenant.Speedster;
import cards.revenant.SpiritOfAsh;
import cards.revenant.RallyingStandard;
import cards.revenant.MimicTear;
import cards.revenant.PartingFlame;
import cards.revenant.PhantomSlash;
import cards.revenant.PowerWithin;
import cards.revenant.PowerOfVengeance;
import cards.revenant.PumpkinHelm;
import cards.revenant.RedHairIcon;
import cards.revenant.RepeatingCrossbow;
import cards.revenant.Seance;
import cards.revenant.StarlightShards;
import cards.revenant.SummonAid;
import cards.revenant.FamilyHeads;
import cards.revenant.Strike_Revenant;
import cards.revenant.Undeath;
import cards.revenant.VisageShield;
import cards.revenant.BlasphemousBlade;
import cards.revenant.SpiritGeyser;
import cards.revenant.WantThis;
import cards.revenant.BudgetTravel;
import cards.revenant.RancorShot;
import cards.revenant.RevengerBlade;
import cards.revenant.BeastclawGreathammer;
import cards.revenant.CodedSword;
import cards.revenant.SoulStifler;
import cards.revenant.CelebrantCleaver;
import cards.revenant.Souleater;
import cards.revenant.SpiritInfusedFist;
import cards.revenant.LongHorn;
import cards.revenant.GraftedDragon;
import cards.revenant.EyeOfDeath;
import cards.revenant.GhostflameCall;
import cards.scholar.AeonianButterfly;
import cards.scholar.AlbinauricBloodclot;
import cards.scholar.AntspurRapier;
import cards.scholar.ArteriaLeaf;
import cards.scholar.Bloodrose;
import cards.scholar.Defend_Scholar;
import cards.scholar.DrawstringPot;
import cards.scholar.Telescope;
import cards.scholar.HeavenCord;
import cards.scholar.ScattershotThrow;
import cards.scholar.Ergodic;
import cards.scholar.EyeOfYelough;
import cards.scholar.FingerprintStoneShield;
import cards.scholar.GoldenCentipede;
import cards.scholar.GravelStone;
import cards.scholar.Homunculus;
import cards.scholar.MiquellaLily;
import cards.scholar.MiniatureStraghess;
import cards.scholar.NascentButterfly;
import cards.scholar.TrinaLily;
import cards.scholar.TranquilWalkOfPeace;
import cards.scholar.WaterHorse;
import cards.scholar.RollingSparks;
import cards.scholar.EquivalentExchange;
import cards.scholar.PenglaiWorship;
import cards.scholar.AjaRedStone;
import cards.scholar.PotionAscetic;
import cards.scholar.PotionCoveredAshes;
import cards.scholar.PotentateCookbook;
import cards.scholar.RiteOfKindling;
import cards.scholar.RoyalLegacy;
import cards.scholar.EmeraldTablet;
import relics.SunStone;
import cards.scholar.RowaFruit;
import cards.scholar.SacramentalBud;
import cards.scholar.Strike_Scholar;
import cards.scholar.Tyranny;
import cards.status.MagicEmber;
import cards.tempcards.CraftmanCreation;
import cards.tempcards.CrucibleToken;
import cards.tempcards.DragonPunch;
import cards.tempcards.FadingPrimalGlintstone;
import cards.tempcards.LotteryTicket;
import cards.tempcards.MoonlightSettles;
import cards.tempcards.MottledPot;
import cards.tempcards.PhantomDoggo;
import cards.tempcards.PhantomFrederick;
import cards.tempcards.PhantomHelen;
import cards.tempcards.PhantomAsimi;
import cards.tempcards.PhantomSebastian;
import cards.wylder.AbyssSeal;
import cards.wylder.AshenEstusFlask;
import cards.wylder.AstoraGreatsword;
import cards.wylder.BlueWhiteWoodenShield;
import cards.wylder.Barehanded;
import cards.wylder.BloodVeil;
import cards.wylder.BloodhoundFinesse;
import cards.wylder.BloodhoundStep;
import cards.wylder.CeruleanDagger;
import cards.wylder.CirqueBlade;
import cards.wylder.ManikinShield;
import cards.wylder.HoneBlade;
import cards.wylder.LightspeedSlash;
import cards.wylder.LifestealFist;
import cards.wylder.StoneArmor;
import cards.wylder.IronFlesh;
import cards.wylder.AdaptiveShield;
import cards.wylder.XanthousCrown;
import cards.wylder.SilvercatRing;
import cards.wylder.SwordOfNight;
import cards.wylder.PutrescenceSword;
import cards.wylder.ChargedBread;
import cards.wylder.TonguesOfFire;
import cards.wylder.SoulOfAbyss;
import cards.wylder.SeaOfMagma;
import cards.wylder.WaveOfDestruction;
import cards.wylder.PardonMe;
import cards.wylder.ExecutionerSword;
import cards.wylder.InTheZone;
import cards.wylder.BanditKnife;
import cards.wylder.Girandole;
import cards.wylder.EternalArmor;
import cards.wylder.Cragblade;
import cards.wylder.Zweihander;
import cards.wylder.GraftedBladeGreatsword;
import cards.wylder.HelphenSteeple;
import cards.wylder.PalmBlast;
import cards.wylder.NoxFlowingShieldTypeI;
import cards.wylder.NoxFlowingShieldTypeP;
import cards.wylder.OnslaughtStake;
import cards.wylder.Onikiri;
import cards.wylder.PitaBread;
import cards.wylder.PowerOfNight;
import cards.wylder.RemoveArmor;
import cards.wylder.SipAbyss;
import cards.wylder.SquareOff;
import cards.undertaker.AlbinauricProof;
import cards.undertaker.AlphaToOmega;
import cards.undertaker.BookOfGenesis;
import cards.undertaker.BookOfRevelation;
import cards.undertaker.BorrowedTime;
import cards.undertaker.ChimeOfWant;
import cards.undertaker.Clockwise;
import cards.undertaker.CleaningAndHosting;
import cards.undertaker.Confluence;
import cards.undertaker.CounterClockwise;
import cards.undertaker.DeadAgain;
import cards.undertaker.DecisionsDecisions;
import cards.undertaker.Denial;
import cards.undertaker.Delirium;
import cards.undertaker.Defend_Undertaker;
import cards.undertaker.Mace;
import cards.undertaker.SpiralhornShield;
import cards.undertaker.EochaidDancingBlade;
import cards.undertaker.StarscourgeGreatsword;
import cards.undertaker.SacredRelicSword;
import cards.undertaker.DemonScar;
import cards.undertaker.Deterrence;
import cards.undertaker.Devourer;
import cards.undertaker.WeedCutter;
import cards.undertaker.Darkeater;
import cards.undertaker.DoThis;
import cards.undertaker.EyesWithin;
import cards.undertaker.Flustered;
import cards.undertaker.RedBearHunt;
import cards.undertaker.Earthshaker;
import cards.undertaker.IronBall;
import cards.undertaker.GoldenParry;
import cards.undertaker.OctopusSlapping;
import cards.undertaker.Slam;
import cards.undertaker.RustedAnchor;
import cards.undertaker.SpinningWeapon;
import cards.undertaker.ForbiddenSun;
import cards.undertaker.Forgotwww;
import cards.undertaker.Filter;
import cards.undertaker.Grant;
import cards.undertaker.YouAreMine;
import cards.undertaker.Blinkbolt;
import cards.undertaker.OverheadStance;
import cards.undertaker.GreatSwampTome;
import cards.undertaker.HowMany;
import cards.undertaker.ImpenetrableThorns;
import cards.undertaker.PowerOfTheLightlessVoid;
import cards.undertaker.LoathsomeHex;
import cards.undertaker.LamentingVisage;
import cards.undertaker.RepeatingThrust;
import cards.undertaker.MakeYourChoice;
import cards.undertaker.GreatStrength;
import cards.undertaker.GreatDexterity;
import cards.undertaker.DeepInsight;
import cards.undertaker.NormalHammer;
import cards.undertaker.OuterBoon;
import cards.undertaker.AllKnowingHelm;
import cards.undertaker.TheGreatBeyond;
import cards.undertaker.PreceptsStrike;
import cards.undertaker.PrescribedLevy;
import cards.undertaker.PoisonedHand;
import cards.undertaker.Purge;
import cards.undertaker.Purification;
import cards.undertaker.SacredPhalanx;
import cards.undertaker.Gravitas;
import cards.undertaker.Sabbath;
import cards.undertaker.Server;
import cards.undertaker.SoulAppease;
import cards.undertaker.Stargaze;
import cards.undertaker.Strike_Undertaker;
import cards.undertaker.TakeoutBox;
import cards.undertaker.Taxes;
import cards.undertaker.TransferPayment;
import cards.undertaker.WantToDrinkThis;
import cards.undertaker.WhatsUp;
import cards.undertaker.Zealotry;
import cards.wylder.ClawShot;
import cards.wylder.CrismonParma;
import cards.wylder.CrowQuills;
import cards.wylder.Darksword;
import cards.wylder.Determination;
import cards.wylder.Defend_Wylder;
import cards.wylder.EstusFlask;
import cards.wylder.FarronTechniques;
import cards.wylder.FumeUltraGreatsword;
import cards.wylder.RingedKnightStraightSword;
import cards.wylder.GrassCrestShield;
import cards.wylder.RingOfFavor;
import cards.wylder.FlynnRing;
import cards.wylder.GravelordSword;
import cards.wylder.GravelordSwordDance;
import cards.wylder.GateOfHorn;
import cards.wylder.Nostalgia;
import cards.wylder.Sacrifice;
import cards.guardian.BrassShield;
import cards.guardian.WingCrestShield;
import cards.guardian.CurseWardGreatshield;
import cards.guardian.DragoncrestGreatshield;
import cards.guardian.MarionetteArmor;
import cards.guardian.WingsOfFreedom;
import cards.guardian.StormBlade;
import cards.guardian.FurPlucking;
import cards.guardian.BorderWall;
import cards.guardian.SolarEnergy;
import cards.guardian.FrozenNeedle;
import cards.guardian.SpikedPalisade;
import cards.guardian.BraggartRoar;
import cards.guardian.EagleAscent;
import cards.guardian.Stormhawk;
import cards.guardian.WarhawkTalon;
import cards.guardian.StarPiercer;
import cards.guardian.PowerOfCompassion;
import cards.wylder.Greatsword;
import cards.wylder.SmallShield;
import cards.wylder.Strike_Wylder;
import cards.wylder.SymbolOfAvarice;
import cards.wylder.TransientCurse;
import cards.wylder.Ubadachi;
import cards.wylder.Valorheart;
import cards.guardian.WolfGreatshield;
import cards.wylder.Solidify;
import cards.wylder.WolfGreatsword;
import cards.wylder.WolfAssault;
import cards.wylder.Warmth;
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
import com.megacrit.cardcrawl.core.CardCrawlGame;
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
import general.CrudeDrug;
import general.DeadEnemyStats;
import general.EnemyBattleStartSnapshot;
import general.ExtraCardRewards;
import general.FragrantBranchOfYoreRewards;
import general.GuidanceStats;
import general.PotionHistory;
import general.RandomUsageCounter;
import general.TransformStats;
import patches.AbstractCardEnum;
import patches.ERNModClassEnum;
import powers.BellowingDragoncrestPower;
import powers.BlendingPower;
import powers.FaithPower;
import powers.InfectionStrikePower;
import powers.InitiativePower;
import powers.LingeringDragoncrestPower;
import powers.PaintWorldPower;
import powers.RedHairIconPower;
import powers.SlumberingDragoncrestPower;
import potions.CloudPotion;
import potions.PlatingPotion;
import potions.BagOfRolls;
import potions.ClarityPotion;
import potions.EndurancePotion;
import potions.CocoaPotion;
import potions.TruffleLiqueur;
import potions.CrimsonPotion;
import potions.DredgeMossClump;
import potions.HeavyPotion;
import potions.BlackflamePotion;
import potions.DevilFruit;
import potions.CalamityPotion;
import potions.PearlPotion;
import potions.DuskHerbDew;
import potions.HaligtreeBalm;
import potions.IntelligencePotion;
import potions.InsightPotion;
import potions.SpiritPotion;
import potions.SummonPotion;
import potions.WalnutMilk;
import potions.OrderPotion;
import potions.SeafoodFeast;
import relics.AlbinauricPot;
import relics.AldiaFund;
import relics.Bagcraft;
import relics.BedOfChaos;
import relics.BeverageGiveaways;
import relics.BequestOfSeath;
import relics.BorrowedLife;
import relics.CinderellaRosette;
import relics.DebtToBond;
import relics.FighterDestined;
import relics.FighterResolve;
import relics.HonorOfPinionfolk;
import relics.SoaringInsignia;
import relics.CelestialWings;
import relics.ShadowOfPorcine;
import relics.SunlightMaggot;
import relics.SunlightMedal;
import relics.OrmaGreatshield;
import relics.TheTwelveBronzeColossi;
import relics.IllusoryRing;
import relics.Jar;
import relics.HermesBoots;
import relics.JosephForesight;
import relics.KleinBottle;
import relics.LaplaceDemon;
import relics.LargeEmber;
import relics.OneTwoPunch;
import relics.Bloodletting;
import relics.LostAshesOfWar;
import relics.LooksLens;
import relics.MagicCocktail;
import relics.MiniatureThunder;
import relics.EagleEye;
import relics.CovetousGoldSerpentRing;
import relics.CovetousSilverSerpentRing;
import relics.NightShard;
import relics.DeepWoodBranch;
import relics.MagnificentPoise;
import relics.OldGreataxe;
import relics.OldWolfRoster;
import relics.OilPot;
import relics.BadApple;
import relics.MalignantTumor;
import relics.PetrifiedDragonBone;
import relics.PrescientGlintEye;
import relics.CalamityWard;
import relics.Suncatcher;
import relics.Tengu;
import relics.RitualBand;
import relics.ScrollOfLogan;
import relics.SacredTimber;
import relics.SeedbedCurse;
import relics.InvigoratingCuredMeat;
import relics.SilverPendant;
import relics.NanoAlloy;
import relics.KnightRing;
import relics.SoulForge;
import relics.TrialSizePackaging;
import relics.TripleBurrows;
import relics.DriedFingers;
import relics.WhetstoneKnife;
import relics.SummonSpirit;
import relics.ToastyMittens;
import relics.Diplopia;
import relics.Brimstone;
import relics.Decalogue;
import relics.MirrorMirror;
import relics.PerpetualMotionMachine;
import relics.Trance;
import relics.TransposingKiln;
import relics.ArsenalCharm;
import relics.StarsDice;
import relics.WhisperingEarring;
import relics.VoiceConduit;
import relics.WheelCatalyst;
import relics.YearsEntwined;
import variables.CrucibleTokenBloodburnVariable;
import variables.LotOfRunesInsightVariable;
import variables.CrucibleTokenPoisonVariable;
import variables.CrucibleTokenScarletRotVariable;
import variables.ScheduledVariable;
import variables.DarkBloodAberrationVariable;

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
        BaseMod.addSaveField(general.ConciliationRewards.SAVE_KEY, new general.ConciliationRewards());
        BaseMod.addDynamicVariable(new CrucibleTokenPoisonVariable());
        BaseMod.addDynamicVariable(new CrucibleTokenScarletRotVariable());
        BaseMod.addDynamicVariable(new CrucibleTokenBloodburnVariable());
        BaseMod.addDynamicVariable(new ScheduledVariable());
        BaseMod.addDynamicVariable(new LotOfRunesInsightVariable());
        BaseMod.addDynamicVariable(new DarkBloodAberrationVariable());
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
        // MOON bottles have no spots texture; a spots color would crash potion rendering.
        BaseMod.addPotion(CalamityPotion.class, new Color(0.42F, 0.20F, 0.56F, 1.0F),
                new Color(0.88F, 0.44F, 0.18F, 1.0F), null,
                CalamityPotion.POTION_ID, ERNModClassEnum.Executor_CLASS);
        BaseMod.addPotion(DevilFruit.class, new Color(0.65F, 0.16F, 0.24F, 1.0F),
                new Color(0.24F, 0.45F, 0.20F, 1.0F), null,
                DevilFruit.POTION_ID, ERNModClassEnum.Executor_CLASS);
        BaseMod.addPotion(BlackflamePotion.class, new Color(0.16F, 0.12F, 0.18F, 1.0F),
                new Color(0.48F, 0.42F, 0.50F, 1.0F), Color.WHITE.cpy(),
                BlackflamePotion.POTION_ID, ERNModClassEnum.Executor_CLASS);
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
        BaseMod.addPotion(HeavyPotion.class, RAIDER_COLOR.cpy(),
                new Color(0.32F, 0.32F, 0.32F, 1.0F), Color.WHITE.cpy(),
                HeavyPotion.POTION_ID, ERNModClassEnum.Raider_CLASS);
        BaseMod.addPotion(PearlPotion.class, new Color(0.91F, 0.91F, 0.96F, 1.0F),
                new Color(0.70F, 0.80F, 0.88F, 1.0F), Color.WHITE.cpy(),
                PearlPotion.POTION_ID, ERNModClassEnum.Raider_CLASS);
        BaseMod.addPotion(CocoaPotion.class, GUARDIAN_COLOR.cpy(),
                new Color(0.32F, 0.18F, 0.10F, 1.0F), Color.WHITE.cpy(),
                CocoaPotion.POTION_ID, ERNModClassEnum.Guardian_CLASS);
        BaseMod.addPotion(TruffleLiqueur.class, new Color(0.65F, 0.42F, 0.24F, 1.0F),
                new Color(0.25F, 0.18F, 0.14F, 1.0F), Color.WHITE.cpy(),
                TruffleLiqueur.POTION_ID, ERNModClassEnum.Guardian_CLASS);
        BaseMod.addPotion(CloudPotion.class, new Color(0.82F, 0.90F, 0.96F, 1.0F),
                new Color(0.58F, 0.73F, 0.84F, 1.0F), Color.WHITE.cpy(),
                CloudPotion.POTION_ID, ERNModClassEnum.Guardian_CLASS);
        BaseMod.addPotion(HaligtreeBalm.class, SCHOLAR_COLOR.cpy(),
                new Color(0.82F, 0.78F, 0.56F, 1.0F), Color.WHITE.cpy(),
                HaligtreeBalm.POTION_ID, ERNModClassEnum.Scholar_CLASS);
        BaseMod.addPotion(InsightPotion.class, UNDERTAKER_COLOR.cpy(),
                new Color(0.48F, 0.48F, 0.58F, 1.0F), Color.WHITE.cpy(),
                InsightPotion.POTION_ID, ERNModClassEnum.Undertaker_CLASS);
        BaseMod.addPotion(SpiritPotion.class, REVENANT_COLOR.cpy(),
                new Color(0.65F, 0.78F, 0.74F, 1.0F), Color.WHITE.cpy(),
                SpiritPotion.POTION_ID, ERNModClassEnum.Revenant_CLASS);
        BaseMod.addPotion(SummonPotion.class, REVENANT_COLOR.cpy(),
                new Color(0.48F, 0.72F, 0.76F, 1.0F), Color.WHITE.cpy(),
                SummonPotion.POTION_ID, ERNModClassEnum.Revenant_CLASS);
        BaseMod.addPotion(WalnutMilk.class, new Color(0.91F, 0.88F, 0.80F, 1.0F),
                REVENANT_COLOR.cpy(), Color.WHITE.cpy(),
                WalnutMilk.POTION_ID, ERNModClassEnum.Revenant_CLASS);
        BaseMod.addPotion(OrderPotion.class, UNDERTAKER_COLOR.cpy(),
                new Color(0.62F, 0.62F, 0.72F, 1.0F), Color.WHITE.cpy(),
                OrderPotion.POTION_ID, ERNModClassEnum.Undertaker_CLASS);
        BaseMod.addPotion(SeafoodFeast.class, UNDERTAKER_COLOR.cpy(),
                new Color(0.36F, 0.52F, 0.62F, 1.0F), Color.WHITE.cpy(),
                SeafoodFeast.POTION_ID, ERNModClassEnum.Undertaker_CLASS);
        BaseMod.addPotion(PlatingPotion.class, WYLDER_COLOR.cpy(),
                new Color(0.55F, 0.62F, 0.64F, 1.0F), Color.WHITE.cpy(),
                PlatingPotion.POTION_ID, ERNModClassEnum.Wylder_CLASS);
        BaseMod.addPotion(BagOfRolls.class, WYLDER_COLOR.cpy(),
                new Color(0.82F, 0.67F, 0.40F, 1.0F), Color.WHITE.cpy(),
                BagOfRolls.POTION_ID, ERNModClassEnum.Wylder_CLASS);
        BaseMod.addPotion(EndurancePotion.class, WYLDER_COLOR.cpy(),
                new Color(0.40F, 0.70F, 0.46F, 1.0F), Color.WHITE.cpy(),
                EndurancePotion.POTION_ID, ERNModClassEnum.Wylder_CLASS);
        BaseMod.addPotion(ClarityPotion.class, DUCHESS_COLOR.cpy(),
                new Color(0.56F, 0.82F, 0.88F, 1.0F), Color.WHITE.cpy(),
                ClarityPotion.POTION_ID, ERNModClassEnum.Duchess_CLASS);
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
        this.cardsToAdd.add(new RingOfFavor());
        this.cardsToAdd.add(new FlynnRing());
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
        this.cardsToAdd.add(new CirqueBlade());
        this.cardsToAdd.add(new FarronTechniques());
        this.cardsToAdd.add(new Valorheart());
        this.cardsToAdd.add(new FumeUltraGreatsword());
        this.cardsToAdd.add(new RingedKnightStraightSword());
        this.cardsToAdd.add(new Greatsword());
        this.cardsToAdd.add(new GravelordSword());
        this.cardsToAdd.add(new GravelordSwordDance());
        this.cardsToAdd.add(new GateOfHorn());
        this.cardsToAdd.add(new Nostalgia());
        this.cardsToAdd.add(new Solidify());
        this.cardsToAdd.add(new Sacrifice());
        this.cardsToAdd.add(new CrowQuills());
        this.cardsToAdd.add(new SymbolOfAvarice());
        this.cardsToAdd.add(new Ubadachi());
        this.cardsToAdd.add(new Onikiri());
        this.cardsToAdd.add(new ManikinShield());
        this.cardsToAdd.add(new HoneBlade());
        this.cardsToAdd.add(new LightspeedSlash());
        this.cardsToAdd.add(new LifestealFist());
        this.cardsToAdd.add(new StoneArmor());
        this.cardsToAdd.add(new IronFlesh());
        this.cardsToAdd.add(new AdaptiveShield());
        this.cardsToAdd.add(new XanthousCrown());
        this.cardsToAdd.add(new SilvercatRing());
        this.cardsToAdd.add(new SwordOfNight());
        this.cardsToAdd.add(new PutrescenceSword());
        this.cardsToAdd.add(new ChargedBread());
        this.cardsToAdd.add(new TonguesOfFire());
        this.cardsToAdd.add(new SoulOfAbyss());
        this.cardsToAdd.add(new SeaOfMagma());
        this.cardsToAdd.add(new WaveOfDestruction());
        this.cardsToAdd.add(new PardonMe());
        this.cardsToAdd.add(new ExecutionerSword());
        this.cardsToAdd.add(new InTheZone());
        this.cardsToAdd.add(new BanditKnife());
        this.cardsToAdd.add(new Girandole());
        this.cardsToAdd.add(new EternalArmor());
        this.cardsToAdd.add(new Cragblade());
        this.cardsToAdd.add(new Zweihander());
        this.cardsToAdd.add(new GraftedBladeGreatsword());
        this.cardsToAdd.add(new HelphenSteeple());
        this.cardsToAdd.add(new PalmBlast());
        this.cardsToAdd.add(new CeruleanDagger());
        this.cardsToAdd.add(new WolfGreatshield());
        this.cardsToAdd.add(new WolfGreatsword());
        this.cardsToAdd.add(new WolfAssault());
        this.cardsToAdd.add(new Warmth());
        this.cardsToAdd.add(new PowerOfNight());
        this.cardsToAdd.add(new AbyssSeal());
        this.cardsToAdd.add(new AshenEstusFlask());
        this.cardsToAdd.add(new Darksword());
        this.cardsToAdd.add(new Determination());
        this.cardsToAdd.add(new EstusFlask());
        this.cardsToAdd.add(new SipAbyss());
        this.cardsToAdd.add(new AstoraGreatsword());
        this.cardsToAdd.add(new SquareOff());
        this.cardsToAdd.add(new Strike_Guardian());
        this.cardsToAdd.add(new Grandeur());
        this.cardsToAdd.add(new Assault());
        this.cardsToAdd.add(new Retaliatory());
        this.cardsToAdd.add(new GolemHalberd());
        this.cardsToAdd.add(new BastardSword());
        this.cardsToAdd.add(new SpearcallRitual());
        this.cardsToAdd.add(new GlintstoneKris());
        this.cardsToAdd.add(new Defend_Guardian());
        this.cardsToAdd.add(new cards.guardian.GuardianWhirlwind());
        this.cardsToAdd.add(new WingCrestShield());
        this.cardsToAdd.add(new WarmingStone());
        this.cardsToAdd.add(new WingsOfSalvation());
        this.cardsToAdd.add(new ChargeForth());
        this.cardsToAdd.add(new FeatherFan());
        this.cardsToAdd.add(new WingedKnightHalberd());
        this.cardsToAdd.add(new BlackKnightHalberd());
        this.cardsToAdd.add(new NeedlePiercer());
        this.cardsToAdd.add(new SpinningGravityThrust());
        this.cardsToAdd.add(new SunlightStraightSword());
        this.cardsToAdd.add(new GoldenHalberd());
        this.cardsToAdd.add(new SplitleafGreatsword());
        this.cardsToAdd.add(new Bonewheel());
        this.cardsToAdd.add(new SpikedShield());
        this.cardsToAdd.add(new MantleOfThorns());
        this.cardsToAdd.add(new HauntingWithin());
        this.cardsToAdd.add(new LessLikelyToBeTargeted());
        this.cardsToAdd.add(new Ridicule());
        this.cardsToAdd.add(new BarricadeShield());
        this.cardsToAdd.add(new Preening());
        this.cardsToAdd.add(new PurpleSign());
        this.cardsToAdd.add(new Stormcaller());
        this.cardsToAdd.add(new CallLightning());
        this.cardsToAdd.add(new Cyclone());
        this.cardsToAdd.add(new Brightbug());
        this.cardsToAdd.add(new ForcedLanding());
        this.cardsToAdd.add(new CrucifixOfTheMadKing());
        this.cardsToAdd.add(new KineticBombardment());
        this.cardsToAdd.add(new WingStance());
        this.cardsToAdd.add(new MillwoodKnightArmor());
        this.cardsToAdd.add(new Typhoon());
        this.cardsToAdd.add(new SunlightShield());
        this.cardsToAdd.add(new GreatshieldOfGlory());
        this.cardsToAdd.add(new FalconShield());
        this.cardsToAdd.add(new Prolong());
        this.cardsToAdd.add(new Aufheben());
        this.cardsToAdd.add(new FatalAppetite());
        this.cardsToAdd.add(new BrassShield());
        this.cardsToAdd.add(new CurseWardGreatshield());
        this.cardsToAdd.add(new DragoncrestGreatshield());
        this.cardsToAdd.add(new MarionetteArmor());
        this.cardsToAdd.add(new PowerOfCompassion());
        this.cardsToAdd.add(new StormWing());
        this.cardsToAdd.add(new WingsOfFreedom());
        this.cardsToAdd.add(new StormBlade());
        this.cardsToAdd.add(new FurPlucking());
        this.cardsToAdd.add(new BorderWall());
        this.cardsToAdd.add(new SolarEnergy());
        this.cardsToAdd.add(new FrozenNeedle());
        this.cardsToAdd.add(new SpikedPalisade());
        this.cardsToAdd.add(new BraggartRoar());
        this.cardsToAdd.add(new EagleAscent());
        this.cardsToAdd.add(new Stormhawk());
        this.cardsToAdd.add(new WarhawkTalon());
        this.cardsToAdd.add(new StarPiercer());
        this.cardsToAdd.add(new BeastRepellentTorch());
        this.cardsToAdd.add(new CrescentMoonAxe());
        this.cardsToAdd.add(new FallingstarBeastJaw());
        this.cardsToAdd.add(new DuelingShield());
        this.cardsToAdd.add(new ShroudingHeavens());
        this.cardsToAdd.add(new Sovereignty());
        this.cardsToAdd.add(new FadeIntoShadow());
        this.cardsToAdd.add(new GiantDoorShield());
        this.cardsToAdd.add(new GowerRingOfProtection());
        this.cardsToAdd.add(new TheBirds());
        this.cardsToAdd.add(new Strike_Ironeye());
        this.cardsToAdd.add(new Defend_Ironeye());
        this.cardsToAdd.add(new CobCannon());
        this.cardsToAdd.add(new BitComet());
        this.cardsToAdd.add(new Marking());
        this.cardsToAdd.add(new PoisonMarking());
        this.cardsToAdd.add(new ShieldOfWant());
        this.cardsToAdd.add(new Lifegem());
        this.cardsToAdd.add(new Leverage());
        this.cardsToAdd.add(new Plunder());
        this.cardsToAdd.add(new BillPlease());
        this.cardsToAdd.add(new MidasTouch());
        this.cardsToAdd.add(new Imitater());
        this.cardsToAdd.add(new StringSecret());
        this.cardsToAdd.add(new SmoothSilkyStone());
        this.cardsToAdd.add(new PetrifiedSomething());
        this.cardsToAdd.add(new CeruleanwhorlBubble());
        this.cardsToAdd.add(new PharrosLockstone());
        this.cardsToAdd.add(new PortableShop());
        this.cardsToAdd.add(new RustedCoin());
        this.cardsToAdd.add(new DeadOrAlive());
        this.cardsToAdd.add(new Barter());
        this.cardsToAdd.add(new RustedGoldCoin());
        this.cardsToAdd.add(new VendingMachine());
        this.cardsToAdd.add(new PlatinumCard());
        this.cardsToAdd.add(new LoyaltyCard());
        this.cardsToAdd.add(new GhostBlade());
        this.cardsToAdd.add(new GhostMillstone());
        this.cardsToAdd.add(new GoldenCrux());
        this.cardsToAdd.add(new SleepEvermore());
        this.cardsToAdd.add(new NineStagesOfDecay());
        this.cardsToAdd.add(new Avelyn());
        this.cardsToAdd.add(new ShotClub());
        this.cardsToAdd.add(new GoughGreatbow());
        this.cardsToAdd.add(new YoungWhiteBranch());
        this.cardsToAdd.add(new RainOfArrows());
        this.cardsToAdd.add(new DivineSpear());
        this.cardsToAdd.add(new Usury());
        this.cardsToAdd.add(new GoldPickledFowlFoot());
        this.cardsToAdd.add(new SilverPickledFowlFoot());
        this.cardsToAdd.add(new Bounty());
        this.cardsToAdd.add(new OldRadiantLifegem());
        this.cardsToAdd.add(new FellowshipAtt());
        this.cardsToAdd.add(new FragrantBranchOfYore());
        this.cardsToAdd.add(new BlackClawAssistant());
        this.cardsToAdd.add(new SnakeSlough());
        this.cardsToAdd.add(new Fluctuation());
        this.cardsToAdd.add(new Entropy());
        this.cardsToAdd.add(new HeatUp());
        this.cardsToAdd.add(new Strike_Raider());
        this.cardsToAdd.add(new Defend_Raider());
        this.cardsToAdd.add(new Club());
        this.cardsToAdd.add(new FourProngedPlow());
        this.cardsToAdd.add(new ForkedHatchet());
        this.cardsToAdd.add(new StormRuler());
        this.cardsToAdd.add(new Stagger());
        this.cardsToAdd.add(new FightThrough());
        this.cardsToAdd.add(new LargeClub());
        this.cardsToAdd.add(new Masterwork());
        this.cardsToAdd.add(new CraftmanHammer());
        this.cardsToAdd.add(new CourtesyEnough());
        this.cardsToAdd.add(new BloodWall());
        this.cardsToAdd.add(new Boomerang());
        this.cardsToAdd.add(new PierceShield());
        this.cardsToAdd.add(new BlessedDew());
        this.cardsToAdd.add(new Siegbrau());
        this.cardsToAdd.add(new SmithingArtSpears());
        this.cardsToAdd.add(new BloodDebt());
        this.cardsToAdd.add(new BloodTax());
        this.cardsToAdd.add(new BlueTearstoneRing());
        this.cardsToAdd.add(new RedTearstoneRing());
        this.cardsToAdd.add(new BleedStone());
        this.cardsToAdd.add(new Boltstone());
        this.cardsToAdd.add(new DarknightStone());
        this.cardsToAdd.add(new Faintstone());
        this.cardsToAdd.add(new FiredrakeStone());
        this.cardsToAdd.add(new Kick());
        this.cardsToAdd.add(new StormKick());
        this.cardsToAdd.add(new HammerTime());
        this.cardsToAdd.add(new MurkyHandScythe());
        this.cardsToAdd.add(new Pickaxe());
        this.cardsToAdd.add(new DemonGreatHammer());
        this.cardsToAdd.add(new BloodforgedSword());
        this.cardsToAdd.add(new PrelateCharge());
        this.cardsToAdd.add(new RawStone());
        this.cardsToAdd.add(new DungPie());
        this.cardsToAdd.add(new TitaniteShard());
        this.cardsToAdd.add(new TitaniteChunk());
        this.cardsToAdd.add(new TitaniteSlab());
        this.cardsToAdd.add(new TwinklingTitanite());
        this.cardsToAdd.add(new DemonTitanite());
        this.cardsToAdd.add(new DragonScale());
        this.cardsToAdd.add(new DragonTorsoStone());
        this.cardsToAdd.add(new DragonHeadStone());
        this.cardsToAdd.add(new DoOrDie());
        this.cardsToAdd.add(new DowsingRod());
        this.cardsToAdd.add(new TotemStela());
        this.cardsToAdd.add(new AcidSurge());
        this.cardsToAdd.add(new PowerOfHouseMarais());
        this.cardsToAdd.add(new Caestus());
        this.cardsToAdd.add(new StoneRing());
        this.cardsToAdd.add(new AssemblyLine());
        this.cardsToAdd.add(new DullEmber());
        this.cardsToAdd.add(new EarthSeeker());
        this.cardsToAdd.add(new HandAxe());
        this.cardsToAdd.add(new BloodfiendArm());
        this.cardsToAdd.add(new GiantCrusher());
        this.cardsToAdd.add(new VordtGreatHammer());
        this.cardsToAdd.add(new GreatClub());
        this.cardsToAdd.add(new MagicStone());
        this.cardsToAdd.add(new MoonOfNokstella());
        this.cardsToAdd.add(new OldMundaneStone());
        this.cardsToAdd.add(new Palestone());
        this.cardsToAdd.add(new PoisonStone());
        this.cardsToAdd.add(new PrismStone());
        this.cardsToAdd.add(new Dismantle());
        this.cardsToAdd.add(new Insurance());
        this.cardsToAdd.add(new CatarinaRoundMoon());
        this.cardsToAdd.add(new DuelistFurledFinger());
        this.cardsToAdd.add(new Smithbox());
        this.cardsToAdd.add(new DuelistGreataxe());
        this.cardsToAdd.add(new WingedKnightTwinaxes());
        this.cardsToAdd.add(new WarMateriel());
        this.cardsToAdd.add(new BoneFist());
        this.cardsToAdd.add(new ExpectAFight());
        this.cardsToAdd.add(new SpinningSlash());
        this.cardsToAdd.add(new Strike_Duchess());
        this.cardsToAdd.add(new Defend_Duchess());
        this.cardsToAdd.add(new DynastFinesse());
        this.cardsToAdd.add(new IceRapier());
        this.cardsToAdd.add(new Misericorde());
        this.cardsToAdd.add(new Bouquet());
        this.cardsToAdd.add(new RuinousGhostflame());
        this.cardsToAdd.add(new BloodflameBlade());
        this.cardsToAdd.add(new ShadowGuard());
        this.cardsToAdd.add(new RaptorOfTheMists());
        this.cardsToAdd.add(new BlackKnife());
        this.cardsToAdd.add(new Darkdrift());
        this.cardsToAdd.add(new GodslayerGreatsword());
        this.cardsToAdd.add(new ScavengerCurvedSword());
        this.cardsToAdd.add(new BlindSpot());
        this.cardsToAdd.add(new StormCurvedSword());
        this.cardsToAdd.add(new BladeOfCalling());
        this.cardsToAdd.add(new BewitchingBranch());
        this.cardsToAdd.add(new Poleblade());
        this.cardsToAdd.add(new cards.duchess.Restage());
        this.cardsToAdd.add(new WaterfowlDance());
        this.cardsToAdd.add(new Mortgage());
        this.cardsToAdd.add(new NoMoney());
        this.cardsToAdd.add(new SlimeMold());
        this.cardsToAdd.add(new MushroomCrown());
        this.cardsToAdd.add(new InvertedStatue());
        this.cardsToAdd.add(new SellswordTwinblades());
        this.cardsToAdd.add(new CeruleanburstCrystal());
        this.cardsToAdd.add(new CrystalEnchanted());
        this.cardsToAdd.add(new EventualGreatness());
        this.cardsToAdd.add(new WindyCrystal());
        this.cardsToAdd.add(new FlowingTechniques());
        this.cardsToAdd.add(new HornetRing());
        this.cardsToAdd.add(new AssassinGambit());
        this.cardsToAdd.add(new WildStrikes());
        this.cardsToAdd.add(new DancerEnchantedSwords());
        this.cardsToAdd.add(new Euporia());
        this.cardsToAdd.add(new GoldTracer());
        this.cardsToAdd.add(new DarkSilverTracer());
        this.cardsToAdd.add(new SwiftSlash());
        this.cardsToAdd.add(new Urumi());
        this.cardsToAdd.add(new HaloScythe());
        this.cardsToAdd.add(new JupiterHeart());
        this.cardsToAdd.add(new PowerOfTheGreaterWill());
        this.cardsToAdd.add(new ConvenientBundlingStrap());
        this.cardsToAdd.add(new SantierSpear());
        this.cardsToAdd.add(new ChillingMist());
        this.cardsToAdd.add(new LineOfStars());
        this.cardsToAdd.add(new DarkWoodGrain());
        this.cardsToAdd.add(new WhiteLightCharge());
        this.cardsToAdd.add(new DancingBlade());
        this.cardsToAdd.add(new Strike_Executor());
        this.cardsToAdd.add(new Defend_Executor());
        this.cardsToAdd.add(new DestinedDeath());
        this.cardsToAdd.add(new BlackFlame());
        this.cardsToAdd.add(new Darkstorm());
        this.cardsToAdd.add(new VenomousFang());
        this.cardsToAdd.add(new RedRustScimitar());
        this.cardsToAdd.add(new BlackFlameTornado());
        this.cardsToAdd.add(new ScarletAeonia());
        this.cardsToAdd.add(new ViperBite());
        this.cardsToAdd.add(new UnalloyedGoldNeedle());
        this.cardsToAdd.add(new Metastasis());
        this.cardsToAdd.add(new BlackFlameRitual());
        this.cardsToAdd.add(new BlackFlameBarrier());
        this.cardsToAdd.add(new BlackFlameBlade());
        this.cardsToAdd.add(new LetFeastBegin());
        this.cardsToAdd.add(new PaintWorld());
        this.cardsToAdd.add(new Blending());
        this.cardsToAdd.add(new AspectsOfTheCrucibleBeast());
        this.cardsToAdd.add(new Numbness());
        this.cardsToAdd.add(new EmitForce());
        this.cardsToAdd.add(new HeartWall());
        this.cardsToAdd.add(new RiversOfBlood());
        this.cardsToAdd.add(new StTrinaSword());
        this.cardsToAdd.add(new HoarfrostStomp());
        this.cardsToAdd.add(new InfectionStrike());
        this.cardsToAdd.add(new CrossInfection());
        this.cardsToAdd.add(new FrenziedBurst());
        this.cardsToAdd.add(new UnendurableFrenzy());
        this.cardsToAdd.add(new BloodboonRitual());
        this.cardsToAdd.add(new BloodhoundFang());
        this.cardsToAdd.add(new ChaosBlade());
        this.cardsToAdd.add(new InseparableSword());
        this.cardsToAdd.add(new RagingBeast());
        this.cardsToAdd.add(new Moonveil());
        this.cardsToAdd.add(new Gnaw());
        this.cardsToAdd.add(new DragonTooth());
        this.cardsToAdd.add(new DeathFlare());
        this.cardsToAdd.add(new SwordOfMilos());
        this.cardsToAdd.add(new SpinningGuillotine());
        this.cardsToAdd.add(new GiantHunt());
        this.cardsToAdd.add(new AlabasterLordsPull());
        this.cardsToAdd.add(new OnyxLordsRepulsion());
        this.cardsToAdd.add(new SolderingIron());
        this.cardsToAdd.add(new SerpentHunter());
        this.cardsToAdd.add(new NomadicFrenzyflame());
        this.cardsToAdd.add(new InescapableFrenzy());
        this.cardsToAdd.add(new Shackle());
        this.cardsToAdd.add(new Seppuku());
        this.cardsToAdd.add(new PowerOfErdtree());
        this.cardsToAdd.add(new AriandelBlood());
        this.cardsToAdd.add(new OrderBlade());
        this.cardsToAdd.add(new DarkBlood());
        this.cardsToAdd.add(new DeadlyDance());
        this.cardsToAdd.add(new HolyGround());
        this.cardsToAdd.add(new FingerprintSpear());
        this.cardsToAdd.add(new WavesOfDarkness());
        this.cardsToAdd.add(new CalamitySpraymist());
        this.cardsToAdd.add(new CrucibleCodex());
        this.cardsToAdd.add(new Expose());
        this.cardsToAdd.add(new FreezingMist());
        this.cardsToAdd.add(new RottenBreath());
        this.cardsToAdd.add(new PosionMothFlight());
        this.cardsToAdd.add(new SnapWillow());
        this.cardsToAdd.add(new NoFlyZone());
        this.cardsToAdd.add(new CalamityExultation());
        this.cardsToAdd.add(new FrenzyflameThrust());
        this.cardsToAdd.add(new CladInBlackflame());
        this.cardsToAdd.add(new ScorpionStinger());
        this.cardsToAdd.add(new Appeasement());
        this.cardsToAdd.add(new BlackFlamestoneParma());
        this.cardsToAdd.add(new Bully());
        this.cardsToAdd.add(new PosionedRelief());
        this.cardsToAdd.add(new GammaKnife());
        this.cardsToAdd.add(new DeathPoker());
        this.cardsToAdd.add(new Strike_Revenant());
        this.cardsToAdd.add(new Defend_Revenant());
        this.cardsToAdd.add(new PhantomSlash());
        this.cardsToAdd.add(new Invoke());
        this.cardsToAdd.add(new BeastClaw());
        this.cardsToAdd.add(new ScaleBearingMerchant());
        this.cardsToAdd.add(new PowerfulWeapon());
        this.cardsToAdd.add(new WatcherStick());
        this.cardsToAdd.add(new ResistanceToAilments());
        this.cardsToAdd.add(new LotOfRunes());
        this.cardsToAdd.add(new MakeYourChoice());
        this.cardsToAdd.add(new GreatStrength());
        this.cardsToAdd.add(new GreatDexterity());
        this.cardsToAdd.add(new DeepInsight());
        this.cardsToAdd.add(new LeadingStrike());
        this.cardsToAdd.add(new LadleStrike());
        this.cardsToAdd.add(new Milady());
        this.cardsToAdd.add(new GreatClingingBone());
        this.cardsToAdd.add(new SnailAdvent());
        this.cardsToAdd.add(new Reimbursement());
        this.cardsToAdd.add(new Mischief());
        this.cardsToAdd.add(new CursedClaws());
        this.cardsToAdd.add(new GhizaWheel());
        this.cardsToAdd.add(new DestitutionAssist());
        this.cardsToAdd.add(new Dirge());
        this.cardsToAdd.add(new Grovewort());
        this.cardsToAdd.add(new ThankYouGift());
        this.cardsToAdd.add(new HorizontalAlliance());
        this.cardsToAdd.add(new VerticalAlliance());
        this.cardsToAdd.add(new Confidence());
        this.cardsToAdd.add(new WorkStudy());
        this.cardsToAdd.add(new CoordinatedAttack());
        this.cardsToAdd.add(new WraithCallingBell());
        this.cardsToAdd.add(new ManikinClaws());
        this.cardsToAdd.add(new Conciliation());
        this.cardsToAdd.add(new SharedOrder());
        this.cardsToAdd.add(new PumpkinHelm());
        this.cardsToAdd.add(new RepeatingCrossbow());
        this.cardsToAdd.add(new BlackButler());
        this.cardsToAdd.add(new GreatEpee());
        this.cardsToAdd.add(new Immolation());
        this.cardsToAdd.add(new Initiative());
        this.cardsToAdd.add(new LightningRam());
        this.cardsToAdd.add(new MoonStance());
        this.cardsToAdd.add(new Speedster());
        this.cardsToAdd.add(new SpiritOfAsh());
        this.cardsToAdd.add(new RallyingStandard());
        this.cardsToAdd.add(new VisageShield());
        this.cardsToAdd.add(new BlasphemousBlade());
        this.cardsToAdd.add(new SpiritGeyser());
        this.cardsToAdd.add(new Seance());
        this.cardsToAdd.add(new Undeath());
        this.cardsToAdd.add(new PowerWithin());
        this.cardsToAdd.add(new PartingFlame());
        this.cardsToAdd.add(new GhostflameIgnition());
        this.cardsToAdd.add(new IScream());
        this.cardsToAdd.add(new StarlightShards());
        this.cardsToAdd.add(new WantThis());
        this.cardsToAdd.add(new BudgetTravel());
        this.cardsToAdd.add(new RancorShot());
        this.cardsToAdd.add(new RevengerBlade());
        this.cardsToAdd.add(new BeastclawGreathammer());
        this.cardsToAdd.add(new CodedSword());
        this.cardsToAdd.add(new SoulStifler());
        this.cardsToAdd.add(new CelebrantCleaver());
        this.cardsToAdd.add(new Souleater());
        this.cardsToAdd.add(new SpiritInfusedFist());
        this.cardsToAdd.add(new LongHorn());
        this.cardsToAdd.add(new GraftedDragon());
        this.cardsToAdd.add(new EyeOfDeath());
        this.cardsToAdd.add(new GhostflameCall());
        this.cardsToAdd.add(new Climax());
        this.cardsToAdd.add(new JellyfishShield());
        this.cardsToAdd.add(new PerfectScore());
        this.cardsToAdd.add(new MoonlightGreatsword());
        this.cardsToAdd.add(new SwordOfNightAndFlame());
        this.cardsToAdd.add(new PowerOfVengeance());
        this.cardsToAdd.add(new SummonAid());
        this.cardsToAdd.add(new FamilyHeads());
        this.cardsToAdd.add(new RedHairIcon());
        this.cardsToAdd.add(new ImpatientBall());
        this.cardsToAdd.add(new DoggoXD());
        this.cardsToAdd.add(new MimicTear());
        this.cardsToAdd.add(new Strike_Scholar());
        this.cardsToAdd.add(new Defend_Scholar());
        this.cardsToAdd.add(new PotionCoveredAshes());
        this.cardsToAdd.add(new Bloodrose());
        this.cardsToAdd.add(new RowaFruit());
        this.cardsToAdd.add(new EyeOfYelough());
        this.cardsToAdd.add(new TranquilWalkOfPeace());
        this.cardsToAdd.add(new GravelStone());
        this.cardsToAdd.add(new AlbinauricBloodclot());
        this.cardsToAdd.add(new GoldenCentipede());
        this.cardsToAdd.add(new WaterHorse());
        this.cardsToAdd.add(new RollingSparks());
        this.cardsToAdd.add(new EquivalentExchange());
        this.cardsToAdd.add(new AeonianButterfly());
        this.cardsToAdd.add(new TrinaLily());
        this.cardsToAdd.add(new MiquellaLily());
        this.cardsToAdd.add(new NascentButterfly());
        this.cardsToAdd.add(new SacramentalBud());
        this.cardsToAdd.add(new ArteriaLeaf());
        this.cardsToAdd.add(new Ergodic());
        this.cardsToAdd.add(new FingerprintStoneShield());
        this.cardsToAdd.add(new DrawstringPot());
        this.cardsToAdd.add(new Telescope());
        this.cardsToAdd.add(new HeavenCord());
        this.cardsToAdd.add(new ScattershotThrow());
        this.cardsToAdd.add(new RiteOfKindling());
        this.cardsToAdd.add(new AntspurRapier());
        this.cardsToAdd.add(new Tyranny());
        this.cardsToAdd.add(new PenglaiWorship());
        this.cardsToAdd.add(new RoyalLegacy());
        this.cardsToAdd.add(new EmeraldTablet());
        this.cardsToAdd.add(new Homunculus());
        this.cardsToAdd.add(new AjaRedStone());
        this.cardsToAdd.add(new PotionAscetic());
        this.cardsToAdd.add(new PotentateCookbook());
        this.cardsToAdd.add(new MiniatureStraghess());
        this.cardsToAdd.add(new Strike_Undertaker());
        this.cardsToAdd.add(new Defend_Undertaker());
        this.cardsToAdd.add(new Mace());
        this.cardsToAdd.add(new SpiralhornShield());
        this.cardsToAdd.add(new Denial());
        this.cardsToAdd.add(new Delirium());
        this.cardsToAdd.add(new TransferPayment());
        this.cardsToAdd.add(new DoThis());
        this.cardsToAdd.add(new Purge());
        this.cardsToAdd.add(new Purification());
        this.cardsToAdd.add(new SacredPhalanx());
        this.cardsToAdd.add(new Gravitas());
        this.cardsToAdd.add(new Filter());
        this.cardsToAdd.add(new LamentingVisage());
        this.cardsToAdd.add(new RepeatingThrust());
        this.cardsToAdd.add(new Confluence());
        this.cardsToAdd.add(new EochaidDancingBlade());
        this.cardsToAdd.add(new StarscourgeGreatsword());
        this.cardsToAdd.add(new SacredRelicSword());
        this.cardsToAdd.add(new Flustered());
        this.cardsToAdd.add(new RedBearHunt());
        this.cardsToAdd.add(new Earthshaker());
        this.cardsToAdd.add(new IronBall());
        this.cardsToAdd.add(new GoldenParry());
        this.cardsToAdd.add(new OctopusSlapping());
        this.cardsToAdd.add(new Slam());
        this.cardsToAdd.add(new RustedAnchor());
        this.cardsToAdd.add(new SpinningWeapon());
        this.cardsToAdd.add(new Forgotwww());
        this.cardsToAdd.add(new BorrowedTime());
        this.cardsToAdd.add(new CleaningAndHosting());
        this.cardsToAdd.add(new Clockwise());
        this.cardsToAdd.add(new CounterClockwise());
        this.cardsToAdd.add(new DemonScar());
        this.cardsToAdd.add(new Grant());
        this.cardsToAdd.add(new YouAreMine());
        this.cardsToAdd.add(new Blinkbolt());
        this.cardsToAdd.add(new OverheadStance());
        this.cardsToAdd.add(new PreceptsStrike());
        this.cardsToAdd.add(new OuterBoon());
        this.cardsToAdd.add(new TheGreatBeyond());
        this.cardsToAdd.add(new AllKnowingHelm());
        this.cardsToAdd.add(new EyesWithin());
        this.cardsToAdd.add(new GreatSwampTome());
        this.cardsToAdd.add(new ForbiddenSun());
        this.cardsToAdd.add(new Zealotry());
        this.cardsToAdd.add(new AlbinauricProof());
        this.cardsToAdd.add(new DecisionsDecisions());
        this.cardsToAdd.add(new Taxes());
        this.cardsToAdd.add(new WhatsUp());
        this.cardsToAdd.add(new Devourer());
        this.cardsToAdd.add(new WeedCutter());
        this.cardsToAdd.add(new Darkeater());
        this.cardsToAdd.add(new ChimeOfWant());
        this.cardsToAdd.add(new PrescribedLevy());
        this.cardsToAdd.add(new PoisonedHand());
        this.cardsToAdd.add(new Sabbath());
        this.cardsToAdd.add(new Deterrence());
        this.cardsToAdd.add(new HowMany());
        this.cardsToAdd.add(new ImpenetrableThorns());
        this.cardsToAdd.add(new PowerOfTheLightlessVoid());
        this.cardsToAdd.add(new Stargaze());
        this.cardsToAdd.add(new WantToDrinkThis());
        this.cardsToAdd.add(new SoulAppease());
        this.cardsToAdd.add(new LoathsomeHex());
        this.cardsToAdd.add(new NormalHammer());
        this.cardsToAdd.add(new Server());
        this.cardsToAdd.add(new DeadAgain());
        this.cardsToAdd.add(new AlphaToOmega());
        this.cardsToAdd.add(new BookOfGenesis());
        this.cardsToAdd.add(new BookOfRevelation());
        this.cardsToAdd.add(new TakeoutBox());
        this.cardsToAdd.add(new GlitstonePebble());
        this.cardsToAdd.add(new FallControl());
        this.cardsToAdd.add(new CannonOfHaima());
        this.cardsToAdd.add(new WitchingHour());
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
        this.cardsToAdd.add(new DragonPunch());
        this.cardsToAdd.add(new CrucibleToken());
        this.cardsToAdd.add(new LotteryTicket());
        this.cardsToAdd.add(new MoonlightSettles());
        this.cardsToAdd.add(new MottledPot());
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
        SunStone sunStone = new SunStone();
        BaseMod.addRelic(sunStone, basemod.helpers.RelicType.SHARED);
        applyRelicDiscoveryMode(sunStone);
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
        addERNRelic(new OldWolfRoster(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new TransposingKiln(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new ArsenalCharm(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new ToastyMittens(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new JosephForesight(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new SilverPendant(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new NanoAlloy(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new KnightRing(), AbstractCardEnum.Wylder_COLOR);
        addERNRelic(new HonorOfPinionfolk(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new SoaringInsignia(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new CelestialWings(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new ShadowOfPorcine(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new SunlightMaggot(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new SunlightMedal(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new OrmaGreatshield(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new TheTwelveBronzeColossi(), AbstractCardEnum.Guardian_COLOR);
        addERNRelic(new EagleEye(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new MiniatureThunder(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new LostAshesOfWar(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new CovetousSilverSerpentRing(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new CovetousGoldSerpentRing(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new AldiaFund(), AbstractCardEnum.Ironeye_COLOR);
        addERNRelic(new FighterResolve(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new OldGreataxe(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new PetrifiedDragonBone(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new Bloodletting(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new BorrowedLife(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new LargeEmber(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new OneTwoPunch(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new DebtToBond(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new FighterDestined(), AbstractCardEnum.Raider_COLOR);
        addERNRelic(new MagnificentPoise(), AbstractCardEnum.Duchess_COLOR);
        addERNRelic(new IllusoryRing(), AbstractCardEnum.Duchess_COLOR);
        addERNRelic(new VoiceConduit(), AbstractCardEnum.Duchess_COLOR);
        addERNRelic(new CalamityWard(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new Suncatcher(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new Tengu(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new StarsDice(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SeedbedCurse(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new InvigoratingCuredMeat(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new OilPot(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new BadApple(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new MalignantTumor(), AbstractCardEnum.Executor_COLOR);
        addERNRelic(new SummonSpirit(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SoulForge(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new TrialSizePackaging(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new TripleBurrows(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new DriedFingers(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new WhetstoneKnife(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new YearsEntwined(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new SacredTimber(), AbstractCardEnum.Revenant_COLOR);
        addERNRelic(new Bagcraft(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new LooksLens(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new AlbinauricPot(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new BeverageGiveaways(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new KleinBottle(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new Jar(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new HermesBoots(), AbstractCardEnum.Scholar_COLOR);
        addERNRelic(new Diplopia(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new Trance(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new PrescientGlintEye(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new LaplaceDemon(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new WhisperingEarring(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new PerpetualMotionMachine(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new Brimstone(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new Decalogue(), AbstractCardEnum.Undertaker_COLOR);
        addERNRelic(new MirrorMirror(), AbstractCardEnum.Undertaker_COLOR);
    }

    private static void addERNRelic(AbstractRelic relic, AbstractCard.CardColor color) {
        general.RelicEnergyDisplay.register(relic.relicId, color);
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
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(Brimstone.ID)
                && pow instanceof FaithPower && target == AbstractDungeon.player && pow.amount > 0) {
            ((Brimstone)AbstractDungeon.player.getRelic(Brimstone.ID)).onFaithGain();
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(SeedbedCurse.ID)) {
            ((SeedbedCurse)AbstractDungeon.player.getRelic(SeedbedCurse.ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(OilPot.ID)) {
            ((OilPot)AbstractDungeon.player.getRelic(OilPot.ID))
                    .onPostPowerApply(pow, target, owner);
        }
        if (target != null && !target.isPlayer && target.hasPower(InfectionStrikePower.POWER_ID)) {
            ((InfectionStrikePower)target.getPower(InfectionStrikePower.POWER_ID))
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
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(InitiativePower.POWER_ID)) {
            ((InitiativePower)AbstractDungeon.player.getPower(InitiativePower.POWER_ID))
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
        if (!CardCrawlGame.loadingSave) general.ConciliationRewards.reset();
        PlayerDebuffStats.resetCombat();
        BedOfMagic.resetCombat();
        EnemyBattleStartSnapshot.reset();
        GuidanceStats.resetCombat();
        DeadEnemyStats.resetCombat();
        PotionHistory.resetCombat();
        FragrantBranchOfYoreRewards.resetCombat();
        ExtraCardRewards.resetCombat();
        RandomUsageCounter.endCombat();
        TransformStats.resetCombat();
        general.SmithingStats.resetCombat();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if (!room.isBattleOver) general.ConciliationRewards.reset();
        PlayerDebuffStats.resetCombat();
        GuidanceStats.resetCombat();
        DeadEnemyStats.resetCombat();
        PotionHistory.resetCombat();
        FragrantBranchOfYoreRewards.resetCombat();
        ExtraCardRewards.resetCombat();
        RandomUsageCounter.resetCombat();
        TransformStats.resetCombat();
        general.SmithingStats.resetCombat();
    }

    @Override
    public void receivePostDraw(AbstractCard arg0) {}

    @Override
    public void receiveEditKeywords() {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            BaseMod.addKeyword("爪牙", new String[] { "爪牙" },
                    "#y爪牙 会在他们的领导者死亡时放弃战斗。");
            BaseMod.addKeyword("重放", new String[] { "重放" },
                    "打出时，额外打出指定次数。由 #y重放 打出的牌不会再次触发 #y重放 。");
            BaseMod.addKeyword("锦囊", new String[] { "锦囊" },
                    "只有当费用为0时可以打出。");
            BaseMod.addKeyword("充能", new String[] { "充能" },
                    "只有当手牌抽满时可以打出。");
            BaseMod.addKeyword("封装", new String[] { "封装" },
                    "选择若干张牌从手牌放入到这张牌的队列牌堆。下一次打出这张牌时，将依次打出队列牌堆的牌的复制品，并将这些牌放回到手牌中。");
            BaseMod.addKeyword("一次性", new String[] { "一次性" },
                    "这张牌在 #y封装 时是 #y消耗牌 。");
            BaseMod.addKeyword("炼制", new String[] { "炼制" },
                    "将 #b3 个 #y药材 效果合成为一张 #y斑斓壶 。");
            BaseMod.addKeyword("药材", new String[] { "药材" },
                    CrudeDrug.buildCrudeDrugKeywordDescription("药材包括： ", " ， ", " 。"));
            BaseMod.addKeyword("艾奥尼亚蝶", new String[] { "艾奥尼亚蝶" },
                    "给予 #b7 层 #y猩红腐败 。");
            BaseMod.addKeyword("血蔷薇", new String[] { "血蔷薇" },
                    "给予 #b2 层 #y出血 。");
            BaseMod.addKeyword("耶罗眼珠", new String[] { "耶罗眼珠" },
                    "给予 #b2 层 #y发狂 。");
            BaseMod.addKeyword("碎石", new String[] { "碎石" },
                    "给予 #b3 层 #y削韧 。");
            BaseMod.addKeyword("白金凝血", new String[] { "白金凝血" },
                    "回复 #b5 生命，获得 #b5 层 #y虚血 。");
            BaseMod.addKeyword("黄金百足", new String[] { "黄金百足" },
                    "获得 [E] 。");
            BaseMod.addKeyword("托莉娜睡莲", new String[] { "托莉娜睡莲" },
                    "给予 #b2 层 #y睡眠 。");
            BaseMod.addKeyword("米凯拉睡莲", new String[] { "米凯拉睡莲" },
                    "敌人在本回合内失去 #b99 点 #y力量 。");
            BaseMod.addKeyword("蜕生蝶", new String[] { "蜕生蝶" },
                    "获得 #b1 层 #y人工制品 。");
            BaseMod.addKeyword("圣血木芽", new String[] { "圣血木芽" },
                    "移除你身上的所有负面效果。");
            BaseMod.addKeyword("亚缇莉亚叶", new String[] { "亚缇莉亚叶" },
                    "在本回合内获得 #b6 点 #y力量 。");
            BaseMod.addKeyword("锻造", new String[] { "锻造" },
                    "使用素材牌对非素材牌 #y质变 或 #y强化 。");
            BaseMod.addKeyword("质变", new String[] { "质变" },
                    "质变素材包括： #y粗刚石 ， #y苍光石 ， #y火龙石 ， #y雷光石 ， #y暗夜石 ， #y毒块石 ， #y血块石 ， #y魔力石 ， #y暗澹古石 ， #y白石 。");
            BaseMod.addKeyword("强化", new String[] { "强化" },
                    "强化素材包括： #y楔形石碎片 ， #y楔形石块 ， #y楔形石原盘 ， #y光辉楔形石 ， #y恶魔楔形石 ， #y龙鳞 。");
            BaseMod.addKeyword("感应", new String[] { "感应" },
                    "通过潜在能力，能比较容易找到稀有牌。");
            BaseMod.addKeyword("代币", new String[] { "代币" },
                    "每当要失去 #y金币 时，失去相同数量的 #y代币 。");
            BaseMod.addKeyword("荆棘", new String[] { "荆棘" },
                    "受到攻击时，对攻击者造成等同于层数的伤害。");
            BaseMod.addKeyword("召唤", new String[] { "召唤" },
                    "召唤包括： #y召唤：海伦 ， #y召唤：弗雷德利克 ， #y召唤：塞巴斯蒂安 ， #y召唤：大狗 ， #y召唤：安诗糸 。");
            BaseMod.addKeyword("基础召唤", new String[] { "基础召唤" },
                    "基础召唤包括： #y召唤：海伦 ， #y召唤：弗雷德利克 ， #y召唤：塞巴斯蒂安 。");
            general.SummonKeywordHelper.register("召唤：大狗", new String[] { "召唤：大狗" }, 2,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影大狗 加入你的手牌，在你的 #y格挡 消失时消散。");
            general.SummonKeywordHelper.register("召唤：安诗糸", new String[] { "召唤：安诗糸" }, 5,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影安诗糸 加入你的手牌，在你的 #y格挡 消失时消散。");
            general.SummonKeywordHelper.register("召唤：阿诗糸", new String[] { "召唤：阿诗糸" }, 5,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影安诗糸 加入你的手牌，在你的 #y格挡 消失时消散。");
            BaseMod.addKeyword("共击", new String[] { "共击" },
                    "只有当对应 #y召唤 时可以打出。");
            general.SummonKeywordHelper.register("召唤：海伦", new String[] { "召唤：海伦" }, 3,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影海伦 加入你的手牌，在你的 #y格挡 消失时消散。");
            general.SummonKeywordHelper.register("召唤：弗雷德利克", new String[] { "召唤：弗雷德利克" }, 8,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影弗雷德利克 加入你的手牌，在你的 #y格挡 消失时消散。");
            general.SummonKeywordHelper.register("召唤：塞巴斯蒂安", new String[] { "召唤：塞巴斯蒂安" }, 5,
                    "获得 #b%d 点 #y格挡 ，将 #b1 张 #y幻影塞巴斯蒂安 加入你的手牌，在你的 #y格挡 消失时消散。");
            BaseMod.addKeyword("南瓜头盔", new String[] { "南瓜头盔" },
                    "如果有 #y召唤：弗雷德利克 ， #y格挡 不再在你的回合开始时消失。");
            BaseMod.addKeyword("连射弩", new String[] { "连射弩" },
                    "如果有 #y召唤：海伦 ， #y幻影海伦 耗能变为 #b0 ，会额外打出 #b2 次。");
            BaseMod.addKeyword("黑执事", new String[] { "黑执事" },
                    "如果有 #y召唤：塞巴斯蒂安 ，在本回合 #y保留 你的手牌。");
            BaseMod.addKeyword("智力", new String[] { "智力" },
                    "#y魔法伤害 和 #y魔法 格挡 获得等同于 #y智力 层数的额外数值。");
            BaseMod.addKeyword("魔法", new String[] { "魔法" },
                    "效果中有“#y魔法”的卡牌。");
            BaseMod.addKeyword("结晶", new String[] { "结晶" },
                    "结晶包括： #y灵魂结晶枪 ， #y结晶卷轴 ， #y结晶环盾 ， #y追踪灵魂结晶块 ， #y崩裂结晶 ， #y重力结晶 。");
            BaseMod.addKeyword("散装", new String[] { "散装" },
                    "在战斗结束时消失。");
            BaseMod.addKeyword("展翼", new String[] { "展翼" },
                    "将受到的攻击或自伤降低 #b50% 。每当你受到未被 #y格挡 的攻击或自伤时，失去 #b1 层。");
            BaseMod.addKeyword("紧闭", new String[] { "紧闭" },
                    "将所受到的伤害降低 #b75% 。你不能打出 #y攻击牌 。");
            BaseMod.addKeyword("振翼", new String[] { "振翼" },
                    "将受到的攻击或自伤降低 #b50% 。每当你受到攻击或自伤时，失去 #b1 层。在层数大于等于 #b10 且小于 #b15 时，获得 #y高空 。在层数大于等于 #b15 时，获得 #y深空 。");
            BaseMod.addKeyword("高空", new String[] { "高空" },
                    "在你的回合开始时，获得 #b1 层 #y冻伤 。造成 #b25% 额外伤害。");
            BaseMod.addKeyword("深空", new String[] { "深空" },
                    "在你的回合开始时，获得 #b2 层 #y冻伤 ，获得 #b1 层 #y振翼 。造成 #b50% 额外伤害。");
            BaseMod.addKeyword("完美格挡", new String[] { "完美格挡" },
                    "持续若干回合。如果敌人攻击结束时你有大于等于 #b15 点 #y格挡 ，获得 #b6 点 #y活力 。");
            BaseMod.addKeyword("弹反", new String[] { "弹反" },
                    "持续若干回合。每当敌人的攻击伤害刚好击破你的 #y格挡 时，在下一角色回合开始前将其意图变为 #y击晕 ，跳过其下个敌人回合，之后恢复原意图。");
            BaseMod.addKeyword("覆甲", new String[] { "覆甲" },
                    "在敌人回合开始时，如果敌人总伤害与你的 #y格挡 之差大于 #b0 且不超过 #y覆甲 层数，获得等同于该差值的 #y格挡 。");
            BaseMod.addKeyword("失衡", new String[] { "失衡" },
                    "如果该敌人的意图包含攻击，且本次行动的攻击没有使你损失生命，则在下一个敌人回合被 #y击晕 ，原意图顺延。");
            BaseMod.addKeyword("活力", new String[] { "活力" },
                    "你的下一次攻击造成额外伤害。");
            BaseMod.addKeyword("骤死", new String[] { "骤死" },
                    "回合结束时，如果 #y骤死 层数大于等于 #b10 ，会触发即死效果。");
            BaseMod.addKeyword("猩红腐败", new String[] { "猩红腐败" },
                    "在回合开始时，像 #y中毒 一样失去生命并减少层数，但会结算两次。");
            BaseMod.addKeyword("燃命", new String[] { "燃命" },
                    "在回合开始时，像 #y中毒 一样失去生命并减少层数，同时失去等量最大生命。");
            BaseMod.addKeyword("击晕", new String[] { "击晕" },
                    "敌人被 #y击晕 时跳过当前行动，并在下回合继续原本意图。");
            BaseMod.addKeyword("虚血", new String[] { "虚血" },
                    "每打出一张 #y攻击牌 减少1层。在你的回合结束和战斗结束时，失去等同于层数的生命。");
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
                    "#y召唤 获得的 #y格挡 和幻影牌的卡面数字增加等同于 #y灵力 的数值。");
            BaseMod.addKeyword("信仰", new String[] { "信仰" },
                    "#y奇术伤害 和 #y奇术 格挡 获得或失去额外数值。");
            BaseMod.addKeyword("奇术", new String[] { "奇术" },
                    "效果中有“#y奇术”的卡牌。");
            BaseMod.addKeyword("冻伤", new String[] { "冻伤" },
                    "每有1层 #y冻伤 ，从 #y攻击 受到的伤害增加 #b20% 。回合开始时，将 #y冻伤 层数减少 #b1 。");
            BaseMod.addKeyword("睡眠", new String[] { "睡眠" },
                    "每有1层 #y睡眠 ，相当于失去 #b1 点 #y力量 。回合结束时，将 #y睡眠 层数减少 #b1 。");
            BaseMod.addKeyword("永眠", new String[] { "永眠" },
                    "在你的回合开始时，如果敌人生命值全满，失去这个效果，否则回复 #b5% 最大生命，#y击晕 #b1 回合。这个效果在敌人受到生命值损伤时失去。");
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
            BaseMod.addKeyword("蘑菇", new String[] { "蘑菇" },
                    "居然是菇！");
            BaseMod.addKeyword("变化", new String[] { "变化" },
                    "将一张牌替换为同角色的一张随机牌。");
        } else {
            BaseMod.addKeyword("Minion", new String[] { "minion", "minions" },
                    "Minions abandon combat without their leader.");
            BaseMod.addKeyword("Replay", new String[] { "replay" },
                    "When played, play this card an additional number of times. Cards played by #yReplay do not trigger #yReplay again.");
            BaseMod.addKeyword("Final Plan", new String[] { "final plan", "finalplan" },
                    "Can only be played when you have 0 Energy.");
            BaseMod.addKeyword("Charged", new String[] { "charged" },
                    "Can only be played when your hand is full.");
            BaseMod.addKeyword("Packaging", new String[] { "packaging" },
                    "Choose cards from your hand and put them into this card's queue. The next time this card is played, play copies of the queued cards in order, then return those cards to your hand.");
            BaseMod.addKeyword("Disposable", new String[] { "disposable" },
                    "This card is an #yExhaust card while #yPackaged.");
            BaseMod.addKeyword("Brew", new String[] { "brew" },
                    "Combine #b3 #yCrude #yDrug effects into a #yMottled #yPot.");
            BaseMod.addKeyword("Crude Drug", new String[] { "crude drug", "crude drugs" },
                    CrudeDrug.buildCrudeDrugKeywordDescription("Crude Drugs include: ", " , ", "."));
            BaseMod.addKeyword("Aeonian Butterfly", new String[] { "aeonian butterfly" },
                    "Apply #b7 #yScarlet #yRot.");
            BaseMod.addKeyword("Bloodrose", new String[] { "bloodrose" },
                    "Apply #b2 #yBloodloss.");
            BaseMod.addKeyword("Eye of Yelough", new String[] { "eye of yelough" },
                    "Apply #b2 #yMadness.");
            BaseMod.addKeyword("Gravel Stone", new String[] { "gravel stone" },
                    "Apply #b3 #yPoise #yBreak.");
            BaseMod.addKeyword("Albinauric Bloodclot", new String[] { "albinauric bloodclot" },
                    "Heal #b5 HP and gain #b5 #yGrey #yHealth.");
            BaseMod.addKeyword("Golden Centipede", new String[] { "golden centipede" },
                    "Gain [E].");
            BaseMod.addKeyword("Trina Lily", new String[] { "trina lily" },
                    "Apply #b2 #ySleep.");
            BaseMod.addKeyword("Miquella Lily", new String[] { "miquella lily" },
                    "Enemy loses #b99 #yStrength this turn.");
            BaseMod.addKeyword("Nascent Butterfly", new String[] { "nascent butterfly" },
                    "Gain #b1 #yArtifact.");
            BaseMod.addKeyword("Sacramental Bud", new String[] { "sacramental bud" },
                    "Remove all negative effects from yourself.");
            BaseMod.addKeyword("Arteria Leaf", new String[] { "arteria leaf" },
                    "Gain #b6 #yStrength this turn.");
            BaseMod.addKeyword("Smithing", new String[] { "smithing" },
                    "Use material cards to #yInfuse or #yReinforce non-material cards.");
            BaseMod.addKeyword("Infusion", new String[] { "infusion" },
                    "Infusion materials include: #yRaw #yStone , #yFaintstone , #yFiredrake #yStone , #yBoltstone , #yDarknight #yStone , #yPoison #yStone , #yBleed #yStone , #yMagic #yStone , #yOld #yMundane #yStone , and #yPalestone .");
            BaseMod.addKeyword("Reinforcement", new String[] { "reinforcement" },
                    "Reinforcement materials include: #yTitanite #yShard , #yTitanite #yChunk , #yTitanite #ySlab , #yTwinkling #yTitanite , #yDemon #yTitanite , and #yDragon #yScale .");
            BaseMod.addKeyword("Arcane", new String[] { "arcane" },
                    "Dormant power helps discover Rare cards.");
            BaseMod.addKeyword("Virtual Currency", new String[] { "virtual currency" },
                    "Whenever you would lose #yGold, lose that much #yVirtual #yCurrency first.");
            BaseMod.addKeyword("Thorns", new String[] { "thorns" },
                    "When attacked, deal damage to the attacker equal to this amount.");
            BaseMod.addKeyword("Summon", new String[] { "summon" },
                    "Summons include: #ySummon: #yHelen , #ySummon: #yFrederick , #ySummon: #ySebastian , #ySummon: #yDoggo , and #ySummon: #yAsimi .");
            BaseMod.addKeyword("Basic Summon", new String[] { "basic summon" },
                    "Basic Summons include: #ySummon: #yHelen , #ySummon: #yFrederick , and #ySummon: #ySebastian .");
            general.SummonKeywordHelper.register("Summon: Helen", new String[] { "summon: helen" }, 3,
                    "Gain #b%d #yBlock, add #b1 #yPhantom #yHelen to your hand, and vanish when your #yBlock is gone.");
            general.SummonKeywordHelper.register("Summon: Frederick", new String[] { "summon: frederick" }, 8,
                    "Gain #b%d #yBlock, add #b1 #yPhantom #yFrederick to your hand, and vanish when your #yBlock is gone.");
            general.SummonKeywordHelper.register("Summon: Sebastian", new String[] { "summon: sebastian" }, 5,
                    "Gain #b%d #yBlock, add #b1 #yPhantom #ySebastian to your hand, and vanish when your #yBlock is gone.");
            general.SummonKeywordHelper.register("Summon: Doggo", new String[] { "summon: doggo" }, 2,
                    "Gain #b%d #yBlock, add #b1 #yPhantom #yDoggo to your hand, and vanish when your #yBlock is gone.");
            general.SummonKeywordHelper.register("Summon: Asimi", new String[] { "summon: asimi" }, 5,
                    "Gain #b%d #yBlock, add #b1 #yPhantom #yAsimi to your hand, and vanish when your #yBlock is gone.");
            BaseMod.addKeyword("Pumpkin Helm", new String[] { "pumpkin helm" },
                    "If you have #ySummon: #yFrederick, #yBlock is not removed at the start of your turn.");
            BaseMod.addKeyword("Repeating Crossbow", new String[] { "repeating crossbow" },
                    "If you have #ySummon: #yHelen, #yPhantom #yHelen costs #b0 and is played #b2 additional times.");
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
                    "Reduces attack damage and self-damage taken by #b50% . Lose #b1 stack whenever you take unblocked attack damage or self-damage.");
            BaseMod.addKeyword("Lockout", new String[] { "lockout" },
                    "Reduce damage taken by #b75% . You cannot play #yAttacks .");
            BaseMod.addKeyword("Hover", new String[] { "hover" },
                    "Reduces attack damage and self-damage taken by #b50% . Lose #b1 stack whenever you take attack damage or self-damage. When stacks are at least #b10 and less than #b15, gain #yLofty. When stacks are at least #b15, gain #yDeep #ySpace.");
            BaseMod.addKeyword("Lofty", new String[] { "lofty" },
                    "At the start of your turn, gain #b1 #yFrostbite. Deal #b25% additional damage.");
            BaseMod.addKeyword("Deep Space", new String[] { "deep space" },
                    "At the start of your turn, gain #b2 #yFrostbite and #b1 #yHover. Deal #b50% additional damage.");
            BaseMod.addKeyword("Perfect Guard", new String[] { "perfect guard" },
                    "Lasts for several turns. If you have at least #b15 #yBlock when enemy attacks finish, gain #b6 #yVigor.");
            BaseMod.addKeyword("Parry", new String[] { "parry" },
                    "Lasts for several turns. Whenever an enemy attack exactly breaks your #yBlock, its intent becomes #yStunned before your next turn. It skips its next turn, then resumes its original intent.");
            BaseMod.addKeyword("Plating", new String[] { "plating" },
                    "At the start of the enemy turn, if total enemy attack damage exceeds your #yBlock by more than #b0 but no more than your #yPlating, gain #yBlock equal to that difference.");
            BaseMod.addKeyword("Posture Break", new String[] { "posture break" },
                    "If this enemy has an attack intent and none of its attacks during that action cause you to lose HP, it is #yStunned on its next turn. Its original intent is delayed.");
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
                    "#yBlock gained from #ySummons and card text numbers on Phantom cards are increased by your #ySpirit.");
            BaseMod.addKeyword("Faith", new String[] { "faith" },
                    "Thaumaturgy damage and Thaumaturgy Block gain or lose additional value.");
            BaseMod.addKeyword("Thaumaturgy", new String[] { "thaumaturgy" },
                    "Cards whose effects contain \"Thaumaturgy\".");
            BaseMod.addKeyword("Frostbite", new String[] { "frostbite" },
                    "For each stack of #yFrostbite, take #b20% more damage from #yAttacks. At the start of turn, reduce #yFrostbite by #b1.");
            BaseMod.addKeyword("Sleep", new String[] { "sleep" },
                    "For each stack of #ySleep, this is equivalent to losing #b1 #yStrength. At the end of turn, reduce #ySleep by #b1.");
            BaseMod.addKeyword("Eternal Sleep", new String[] { "eternal sleep" },
                    "At the start of this enemy's turn, remove this effect if it is at full HP. Otherwise, it heals #b5% of its max HP (rounded up) and is stunned for #b1 turn. Removed when the enemy loses HP.");
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
            BaseMod.addKeyword("Mushroom", new String[] { "mushroom" },
                    "It is actually a mushroom!");
            BaseMod.addKeyword("Transform", new String[] { "transform" },
                    "Replace a card with a random card from the same character.");
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        GuidanceStats.recordCardUse(abstractCard);
        if (BedOfMagic.ID.equals(general.SmithingBody.behavior(abstractCard).cardID)) {
            return;
        }
        BedOfMagic.recordPlayedCard(abstractCard);
    }

    @Override
    public void receivePostBattle(AbstractRoom r) {
        PlayerDebuffStats.resetCombat();
        BedOfMagic.resetCombat();
        EnemyBattleStartSnapshot.reset();
        DeadEnemyStats.resetCombat();
        PotionHistory.resetCombat();
        RandomUsageCounter.endCombat();
        TransformStats.resetCombat();
        general.SmithingStats.resetCombat();
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
