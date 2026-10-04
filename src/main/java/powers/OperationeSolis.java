package powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import patches.OperationeSolisPatch;
import relics.SunStone;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class OperationeSolis extends AbstractPower {
    public static final String POWER_ID = "OperationeSolis";
    public static final int FINAL_STAGE = 14;
    private static final int EMERALD_TABLET_STAGE = 6;
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int stage = 1;
    private boolean gainedHover;
    private boolean gainedVigor;
    private boolean awaitingBlockAfterStatus;
    private boolean intervalOpen;
    private int intervalEffects;
    private int shufflesInTurn;
    private AbstractCard observedDrawTop;
    private AbstractCard observedDiscardTop;
    private String observedDrawName;
    private String observedDiscardName;
    private int observedDrawUpgrades;
    private int observedDiscardUpgrades;
    private boolean observedDrawUpgraded;
    private boolean observedDiscardUpgraded;

    public OperationeSolis(AbstractPlayer owner) {
        this.owner = owner;
        ID = POWER_ID;
        name = STRINGS.NAME;
        type = PowerType.BUFF;
        amount = -1;
        description = "";
    }

    public static OperationeSolis obtain(AbstractPlayer player) {
        AbstractPower existing = player.getPower(POWER_ID);
        if (existing instanceof OperationeSolis) {
            return (OperationeSolis)existing;
        }
        OperationeSolis power = new OperationeSolis(player);
        player.powers.add(power);
        power.onInitialApplication();
        return power;
    }

    @Override
    public void onInitialApplication() {
        if (((AbstractPlayer)owner).hasRelic(SunStone.ID)) {
            stage = FINAL_STAGE;
        }
        checkTruth();
    }

    @Override
    public void stackPower(int stackAmount) {
        // Reacquiring this hidden quest must not reset or advance it.
    }

    public void onEmeraldTabletCardGenerated(AbstractCard card) {
        if (stage == EMERALD_TABLET_STAGE && card.type == AbstractCard.CardType.ATTACK
                && card.cost >= 3) {
            advanceStage();
        }
    }

    public int getStage() {
        return stage;
    }

    public void checkTruth() {
        if (stage == 1 && ((AbstractPlayer)owner).hasRelic("Philosopher's Stone")) advanceStage();
    }

    public void onStageCardPlayed(AbstractCard card) {
        if ((stage == 1 && "Apotheosis".equals(card.cardID))
                || (stage == 2 && "InvertedStatue".equals(card.cardID))
                || (stage == 3 && "All For One".equals(card.cardID))
                || (stage == 4 && "SunlightStraightSword".equals(card.cardID))
                || (stage == 6 && "WatcherStick".equals(card.cardID))
                || (stage == 8 && ("Ictarus".equals(card.cardID) || "BloodSigil".equals(card.cardID)
                    || "QuellStorm".equals(card.cardID)))
                || (stage == 12 && "Magnetism".equals(card.cardID))) advanceStage();
    }

    public void onOverkillAttack(int damageAmount) {
        if (stage == 6 && damageAmount >= 99) advanceStage();
    }

    public void checkAltitude() {
        if (stage == 8 && (owner.hasPower("LoftyPower") || owner.hasPower("DeepSpacePower"))) advanceStage();
    }

    public void checkGlory() {
        if (stage == 9 && (owner.maxHealth >= 80 || CardCrawlGame.goldGained > 666
                || CardCrawlGame.champion > 0 || CardCrawlGame.perfect > 0)) advanceStage();
    }

    public void checkObstacles() {
        if (stage == 10 && "TheEnding".equals(AbstractDungeon.id)) advanceStage();
    }

    public void checkFortitude() {
        if (stage != 11) return;
        AbstractPlayer player = (AbstractPlayer)owner;
        AbstractPower strength = player.getPower("Strength");
        if ((strength != null && strength.amount >= 10) || player.masterDeck.size() <= 15
                || player.hasPower("Metallicize") || player.hasPower("Focus")
                || player.hasPower("Barricade") || player.hasPower("Plated Armor")) {
            advanceStage();
            return;
        }
        for (com.megacrit.cardcrawl.orbs.AbstractOrb orb : player.orbs) {
            if ("Frost".equals(orb.ID)) {
                advanceStage();
                return;
            }
        }
    }

    public void onEnemyStunned() {
        if (stage == 10) advanceStage();
    }

    public void onCreationCard() { if (stage == 12) advanceStage(); }

    public void onBasicCardsTurn() { if (stage == 12) advanceStage(); }

    public void beginPlayerTurn() { shufflesInTurn = 0; }

    public void onShuffle() {
        if (stage == 13 && ++shufflesInTurn >= 3) advanceStage();
    }

    public void checkHermes() {
        if (stage == 13 && ((AbstractPlayer)owner).hasRelic("WingedGreaves")) advanceStage();
    }

    public void onTurnWithoutCardBlock() {
        if (stage == 10) advanceStage();
    }

    public void onMatchingTurns() {
        if (stage == 2) advanceStage();
    }

    public void onCardExhausted(AbstractCard card) {
        onIntervalEffect(1, 1);
        if (stage != 1 || card.type != AbstractCard.CardType.CURSE) return;
        AbstractPlayer player = (AbstractPlayer)owner;
        for (CardGroup pile : new CardGroup[] {player.hand, player.drawPile, player.discardPile}) {
            for (AbstractCard remaining : pile.group) {
                if (remaining.type == AbstractCard.CardType.CURSE) return;
            }
        }
        advanceStage();
    }

    public void checkGeneratedColors() {
        if (stage == 3 && OperationeSolisPatch.CombatFields.generated.get(owner).isComplete()) {
            advanceStage();
        }
        checkMatchingCards();
    }

    public void checkMatchingCards() {
        if (stage != 3) return;
        AbstractPlayer player = (AbstractPlayer)owner;
        Map<OperationeSolisPatch.CardIdentity, Integer> counts = new HashMap<>();
        Set<AbstractCard> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (CardGroup pile : new CardGroup[] {player.drawPile, player.discardPile, player.hand}) {
            for (AbstractCard card : pile.group) {
                // A move can briefly leave the same instance referenced in two groups.
                if (!seen.add(card)) continue;
                OperationeSolisPatch.CardIdentity key = new OperationeSolisPatch.CardIdentity(card);
                int count = counts.getOrDefault(key, 0) + 1;
                if (count >= 5) {
                    advanceStage();
                    return;
                }
                counts.put(key, count);
            }
        }
    }

    public void onIntervalEffect(int effect, int actualAmount) {
        if (stage != 4 || !intervalOpen || actualAmount <= 0) return;
        intervalEffects |= effect;
        if (intervalEffects == 15) advanceStage();
    }

    public void checkPotionEffects() {
        if (stage == 4 && general.PotionTaskHistory.isComplete()) advanceStage();
    }

    public void checkForeknowledge() {
        AbstractPlayer player = (AbstractPlayer)owner;
        if (stage == 5 && (player.hasRelic("Frozen Eye") || player.hasPower("InsightPower")
                || OperationeSolisPatch.CombatFields.placedOnTop.get(player))) {
            advanceStage();
        }
    }

    public void checkSeparation() {
        if (stage == 7 && ((OperationeSolisPatch.CombatFields.playedThaumaturgy.get(owner)
                && OperationeSolisPatch.CombatFields.playedMagic.get(owner))
                || (owner.hasPower("FaithPower") && owner.hasPower("IntelligencePower")))) {
            advanceStage();
        }
    }

    public void beforeCardPlayed() {
        awaitingBlockAfterStatus = false;
        intervalOpen = stage == 4;
        intervalEffects = 0;
    }

    public void beforeCardExhausted(AbstractCard card) {
        if (stage == 7 && card.type == AbstractCard.CardType.STATUS) {
            // Open before onExhaust callbacks, including immediate block from other effects.
            awaitingBlockAfterStatus = true;
        }
    }

    public void onBlockGained(int amount) {
        onIntervalEffect(8, amount);
        if (stage == 7 && awaitingBlockAfterStatus && amount > 0) advanceStage();
    }

    public void onPowerGained(AbstractPower power, int amount) {
        if (stage == 13 && "SummonAsimi".equals(power.ID)) {
            advanceStage();
            return;
        }
        if (stage != 8 || amount <= 0) {
            return;
        }
        if ("HoverPower".equals(power.ID)) gainedHover = true;
        if ("Vigor".equals(power.ID)) gainedVigor = true;
        if (gainedHover && gainedVigor) advanceStage();
    }

    public void onPilesChanged(boolean enteringStage) {
        checkMatchingCards();
        if (stage != 2) return;
        AbstractPlayer player = (AbstractPlayer)owner;
        AbstractCard draw = player.drawPile.isEmpty() ? null : player.drawPile.getTopCard();
        AbstractCard discard = player.discardPile.isEmpty() ? null : player.discardPile.getTopCard();
        // Action-boundary notifications also cover direct list edits, without rescanning either pile.
        boolean changed = enteringStage || draw != observedDrawTop || discard != observedDiscardTop
                || (draw != null && (!java.util.Objects.equals(draw.name, observedDrawName)
                    || draw.timesUpgraded != observedDrawUpgrades || draw.upgraded != observedDrawUpgraded))
                || (discard != null && (!java.util.Objects.equals(discard.name, observedDiscardName)
                    || discard.timesUpgraded != observedDiscardUpgrades || discard.upgraded != observedDiscardUpgraded));
        if (!changed) return;
        observedDrawTop = draw;
        observedDiscardTop = discard;
        observedDrawName = draw == null ? null : draw.name;
        observedDiscardName = discard == null ? null : discard.name;
        observedDrawUpgrades = draw == null ? 0 : draw.timesUpgraded;
        observedDiscardUpgrades = discard == null ? 0 : discard.timesUpgraded;
        observedDrawUpgraded = draw != null && draw.upgraded;
        observedDiscardUpgraded = discard != null && discard.upgraded;
        if (draw != null && discard != null
                && new OperationeSolisPatch.CardIdentity(draw).equals(new OperationeSolisPatch.CardIdentity(discard))) {
            advanceStage();
        }
    }

    public int getAttacksInStage() {
        return 0;
    }

    public void advanceStage() {
        if (stage >= FINAL_STAGE) {
            return;
        }
        stage++;
        shufflesInTurn = 0;
        gainedHover = false;
        gainedVigor = false;
        awaitingBlockAfterStatus = false;
        intervalOpen = false;
        intervalEffects = 0;
        onPilesChanged(true);
        checkGeneratedColors();
        checkPotionEffects();
        checkForeknowledge();
        checkSeparation();
        checkAltitude();
        checkGlory();
        checkObstacles();
        checkFortitude();
        checkHermes();
        if (stage == FINAL_STAGE) {
            final AbstractPlayer player = (AbstractPlayer)owner;
            if (!OperationeSolisPatch.CombatFields.completed.get(player)) {
                OperationeSolisPatch.CombatFields.completed.set(player, true);
                if (!player.hasRelic(SunStone.ID)) {
                    addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            if (!player.hasRelic(SunStone.ID)) {
                                new SunStone().instantObtain(player, player.relics.size(), true);
                            }
                            isDone = true;
                        }
                    });
                }
            }
        }
    }

    public String getStageText() {
        return STRINGS.DESCRIPTIONS[stage - 1];
    }

    public void showCurrentStageText() {
        AbstractPlayer player = (AbstractPlayer)owner;
        AbstractDungeon.effectList.add(new ThoughtBubble(player.dialogX, player.dialogY,
                6.0F, getStageText(), true));
    }

    @Override
    public void flash() {}

    @Override
    public void flashWithoutSound() {}

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color color) {}

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color color) {}
}
