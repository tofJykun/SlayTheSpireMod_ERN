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
import cards.duchess.Strike_Duchess;
import cards.executor.BlackFlame;
import cards.executor.BlackFlameBlade;
import cards.executor.BlackFlameRitual;
import cards.executor.BlackFlameTornado;
import cards.executor.DestinedDeath;
import cards.executor.Defend_Executor;
import cards.executor.LetFeastBegin;
import cards.executor.Strike_Executor;
import cards.guardian.ChargeForth;
import cards.guardian.Defend_Guardian;
import cards.guardian.Strike_Guardian;
import cards.guardian.WarmingStone;
import cards.guardian.WingsOfSalvation;
import cards.ironeye.Defend_Ironeye;
import cards.ironeye.Strike_Ironeye;
import cards.raider.Defend_Raider;
import cards.raider.Strike_Raider;
import cards.recluse.BedOfMagic;
import cards.recluse.CarianPiercer;
import cards.recluse.CarianSlicer;
import cards.recluse.CometAzur;
import cards.recluse.CrystalScroll;
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
import cards.recluse.GreatSoulDregs;
import cards.recluse.ElementalDefense;
import cards.recluse.HiddenBody;
import cards.recluse.HomingCrystalSoulmass;
import cards.recluse.LawOfRegression;
import cards.recluse.MagicBarrier;
import cards.recluse.MagicShield;
import cards.recluse.MorionBlade;
import cards.recluse.PrimalGlintstone;
import cards.recluse.ReignitedCinder;
import cards.recluse.SoulBolt;
import cards.recluse.SoulbloodSong;
import cards.recluse.SoulFlash;
import cards.recluse.SoulGeyser;
import cards.recluse.StoneClutch;
import cards.recluse.StrongMagicShield;
import cards.recluse.Strike_Recluse;
import cards.recluse.SwiftGlintstoneShard;
import cards.recluse.WhiteLightning;
import cards.recluse.Yearn;
import cards.revenant.Defend_Revenant;
import cards.revenant.Strike_Revenant;
import cards.scholar.AntspurRapier;
import cards.scholar.Defend_Scholar;
import cards.scholar.PenglaiWorship;
import cards.scholar.PotionCoveredAshes;
import cards.scholar.Strike_Scholar;
import cards.scholar.Tyranny;
import cards.status.MagicEmber;
import cards.tempcards.FadingPrimalGlintstone;
import cards.tempcards.PhantomFrederick;
import cards.tempcards.PhantomHelen;
import cards.tempcards.PhantomSebastian;
import cards.wylder.BlueWhiteWoodenShield;
import cards.undertaker.Delirium;
import cards.undertaker.Defend_Undertaker;
import cards.undertaker.Strike_Undertaker;
import cards.wylder.ClawShot;
import cards.wylder.CrismonParma;
import cards.wylder.Defend_Wylder;
import cards.wylder.GrassCrestShield;
import cards.wylder.SmallShield;
import cards.wylder.Strike_Wylder;
import cards.wylder.TransientCurse;
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
import relics.Bagcraft;
import relics.BedOfChaos;
import relics.BequestOfSeath;
import relics.BorrowedLife;
import relics.DebtToBond;
import relics.FighterDestined;
import relics.FighterResolve;
import relics.HonorOfPinionfolk;
import relics.MagicCocktail;
import relics.Marking;
import relics.OolacileIvoryCatalyst;
import relics.Restage;
import relics.ScrollOfLogan;
import relics.SummonSpirit;
import relics.Suncatcher;
import relics.Trance;

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
    private final ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
    public static ArrayList<AbstractCard> recyclecards = new ArrayList<>();

    public ERNMod() {
        BaseMod.subscribe((ISubscriber)this);
        BaseMod.addSaveField(BulkPotionQueue.SAVE_KEY, BulkPotionQueue.SAVE_FIELD);
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
        this.cardsToAdd.add(new Strike_Guardian());
        this.cardsToAdd.add(new Defend_Guardian());
        this.cardsToAdd.add(new cards.guardian.Whirlwind());
        this.cardsToAdd.add(new WarmingStone());
        this.cardsToAdd.add(new WingsOfSalvation());
        this.cardsToAdd.add(new ChargeForth());
        this.cardsToAdd.add(new Strike_Ironeye());
        this.cardsToAdd.add(new Defend_Ironeye());
        this.cardsToAdd.add(new Strike_Raider());
        this.cardsToAdd.add(new Defend_Raider());
        this.cardsToAdd.add(new Strike_Duchess());
        this.cardsToAdd.add(new Defend_Duchess());
        this.cardsToAdd.add(new DynastFinesse());
        this.cardsToAdd.add(new Strike_Executor());
        this.cardsToAdd.add(new Defend_Executor());
        this.cardsToAdd.add(new DestinedDeath());
        this.cardsToAdd.add(new BlackFlame());
        this.cardsToAdd.add(new BlackFlameTornado());
        this.cardsToAdd.add(new BlackFlameRitual());
        this.cardsToAdd.add(new BlackFlameBlade());
        this.cardsToAdd.add(new LetFeastBegin());
        this.cardsToAdd.add(new Strike_Revenant());
        this.cardsToAdd.add(new Defend_Revenant());
        this.cardsToAdd.add(new Strike_Scholar());
        this.cardsToAdd.add(new Defend_Scholar());
        this.cardsToAdd.add(new PotionCoveredAshes());
        this.cardsToAdd.add(new AntspurRapier());
        this.cardsToAdd.add(new Tyranny());
        this.cardsToAdd.add(new PenglaiWorship());
        this.cardsToAdd.add(new Strike_Undertaker());
        this.cardsToAdd.add(new Defend_Undertaker());
        this.cardsToAdd.add(new Delirium());
        this.cardsToAdd.add(new GlitstonePebble());
        this.cardsToAdd.add(new FallControl());
        this.cardsToAdd.add(new SoulFlash());
        this.cardsToAdd.add(new SoulBolt());
        this.cardsToAdd.add(new SoulGeyser());
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
        this.cardsToAdd.add(new CrystalSoulSpear());
        this.cardsToAdd.add(new MorionBlade());
        this.cardsToAdd.add(new WhiteLightning());
        this.cardsToAdd.add(new ElementalDefense());
        this.cardsToAdd.add(new SoulbloodSong());
        this.cardsToAdd.add(new Eagle());
        this.cardsToAdd.add(new PrimalGlintstone());
        this.cardsToAdd.add(new ReignitedCinder());
        this.cardsToAdd.add(new LawOfRegression());
        this.cardsToAdd.add(new FadingPrimalGlintstone());
        this.cardsToAdd.add(new PhantomHelen());
        this.cardsToAdd.add(new PhantomFrederick());
        this.cardsToAdd.add(new PhantomSebastian());
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
        BaseMod.addRelicToCustomPool(new MagicCocktail(), AbstractCardEnum.Recluse_COLOR);
        BaseMod.addRelicToCustomPool(new BedOfChaos(), AbstractCardEnum.Recluse_COLOR);
        BaseMod.addRelicToCustomPool(new BequestOfSeath(), AbstractCardEnum.Recluse_COLOR);
        BaseMod.addRelicToCustomPool(new ScrollOfLogan(), AbstractCardEnum.Recluse_COLOR);
        BaseMod.addRelicToCustomPool(new OolacileIvoryCatalyst(), AbstractCardEnum.Recluse_COLOR);
        BaseMod.addRelicToCustomPool(new relics.SixthSense(), AbstractCardEnum.Wylder_COLOR);
        BaseMod.addRelicToCustomPool(new HonorOfPinionfolk(), AbstractCardEnum.Guardian_COLOR);
        BaseMod.addRelicToCustomPool(new Marking(), AbstractCardEnum.Ironeye_COLOR);
        BaseMod.addRelicToCustomPool(new FighterResolve(), AbstractCardEnum.Raider_COLOR);
        BaseMod.addRelicToCustomPool(new BorrowedLife(), AbstractCardEnum.Raider_COLOR);
        BaseMod.addRelicToCustomPool(new DebtToBond(), AbstractCardEnum.Raider_COLOR);
        BaseMod.addRelicToCustomPool(new FighterDestined(), AbstractCardEnum.Raider_COLOR);
        BaseMod.addRelicToCustomPool(new Restage(), AbstractCardEnum.Duchess_COLOR);
        BaseMod.addRelicToCustomPool(new Suncatcher(), AbstractCardEnum.Executor_COLOR);
        BaseMod.addRelicToCustomPool(new SummonSpirit(), AbstractCardEnum.Revenant_COLOR);
        BaseMod.addRelicToCustomPool(new Bagcraft(), AbstractCardEnum.Scholar_COLOR);
        BaseMod.addRelicToCustomPool(new Trance(), AbstractCardEnum.Undertaker_COLOR);
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
    public void receivePostPowerApplySubscriber(AbstractPower pow, AbstractCreature target, AbstractCreature owner) {}

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
            BaseMod.addKeyword("召唤：海伦", new String[] { "召唤：海伦" },
                    "获得 #b3 点格挡，将1张 #y幻影海伦 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：弗雷德利克", new String[] { "召唤：弗雷德利克" },
                    "获得 #b8 点格挡，将1张 #y幻影弗雷德利克 加入你的手牌，在你的格挡消失时消散。");
            BaseMod.addKeyword("召唤：塞巴斯蒂安", new String[] { "召唤：塞巴斯蒂安" },
                    "获得 #b5 点格挡，将1张 #y幻影塞巴斯蒂安 加入你的手牌，在你的格挡消失时消散。");
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
        } else {
            BaseMod.addKeyword("Summon: Helen", new String[] { "summon: helen" },
                    "Gain #b3 Block, add 1 #yPhantom #yHelen to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Frederick", new String[] { "summon: frederick" },
                    "Gain #b8 Block, add 1 #yPhantom #yFrederick to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Summon: Sebastian", new String[] { "summon: sebastian" },
                    "Gain #b5 Block, add 1 #yPhantom #ySebastian to your hand, and vanish when your Block is gone.");
            BaseMod.addKeyword("Phantom Helen", new String[] { "phantom helen" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #yHelen.");
            BaseMod.addKeyword("Phantom Frederick", new String[] { "phantom frederick" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #yFrederick.");
            BaseMod.addKeyword("Phantom Sebastian", new String[] { "phantom sebastian" },
                    "A #ySpecial Attack that #yExhausts and is #yEthereal. Can only be played while you have #ySummon: #ySebastian.");
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
