package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PhilosopherStone;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import powers.OperationeSolis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.Objects;

public class OperationeSolisPatch {
    private static int topPlacementSuppression;

    public static void suppressTopPlacement() { topPlacementSuppression++; }
    public static void restoreTopPlacement() { topPlacementSuppression--; }

    public static void recordTopPlacement(CardGroup pile) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || pile != player.drawPile || pile.isEmpty()
                || topPlacementSuppression > 0 || !CombatFields.generated.get(player).active) return;
        CombatFields.placedOnTop.set(player, true);
        checkForeknowledge();
    }

    public static void checkForeknowledge() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkForeknowledge();
    }

    public static void checkSeparation() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkSeparation();
    }

    public static void checkTruth() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkTruth();
    }

    public static void checkAltitude() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkAltitude();
    }

    public static void checkGlory() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkGlory();
    }

    public static void checkObstacles() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkObstacles();
    }

    public static void checkFortitude() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkFortitude();
    }

    public static void checkHermes() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkHermes();
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb",
            paramtypez = {com.megacrit.cardcrawl.orbs.AbstractOrb.class})
    public static class FortitudeOrbChanneled {
        @SpirePostfixPatch
        public static void postfix() { checkFortitude(); }
    }

    @SpirePatches({
        @SpirePatch(clz = AbstractPower.class, method = "addToBot"),
        @SpirePatch(clz = AbstractPower.class, method = "addToTop"),
        @SpirePatch(clz = AbstractRelic.class, method = "addToBot"),
        @SpirePatch(clz = AbstractRelic.class, method = "addToTop"),
        @SpirePatch(clz = com.megacrit.cardcrawl.potions.AbstractPotion.class, method = "addToBot"),
        @SpirePatch(clz = com.megacrit.cardcrawl.potions.AbstractPotion.class, method = "addToTop")
    })
    public static class NonCardBlockSource {
        @SpirePostfixPatch
        public static void postfix(Object __instance, com.megacrit.cardcrawl.actions.AbstractGameAction action) {
            general.SolarCardBlockHistory.nonCardAction(action);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp",
            paramtypez = {int.class, boolean.class})
    public static class GloryMaxHealth {
        @SpirePostfixPatch
        public static void postfix(AbstractCreature __instance) {
            if (__instance == AbstractDungeon.player) checkGlory();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "gainGold", paramtypez = {int.class})
    public static class GloryGold {
        @SpirePostfixPatch
        public static void postfix() { checkGlory(); }
    }

    @SpirePatch(clz = com.megacrit.cardcrawl.rooms.AbstractRoom.class, method = "endBattle")
    public static class GloryEliteVictory {
        @SpirePostfixPatch
        public static void postfix() { checkGlory(); }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "onBossVictoryLogic")
    public static class GloryBossVictory {
        @SpirePostfixPatch
        public static void postfix() { checkGlory(); }
    }

    public static final class CardIdentity {
        private final String name;
        private final boolean upgraded;
        private final int upgrades;

        public CardIdentity(AbstractCard card) {
            name = card.name;
            upgraded = card.upgraded;
            upgrades = card.timesUpgraded;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof CardIdentity)) return false;
            CardIdentity that = (CardIdentity)other;
            return Objects.equals(name, that.name) && upgraded == that.upgraded && upgrades == that.upgrades;
        }

        @Override
        public int hashCode() { return Objects.hash(name, upgraded, upgrades); }
    }

    public static class TurnHistory {
        private ArrayList<CardIdentity> previous;
        private final ArrayList<CardIdentity> current = new ArrayList<>();
        private boolean open = true;
        private boolean onlyBasics = true;
        private boolean basicPlayedInCreation;

        public void played(AbstractCard card) {
            if (!open) return;
            current.add(new CardIdentity(card));
            boolean basic = card.hasTag(AbstractCard.CardTags.STARTER_STRIKE)
                    || card.hasTag(AbstractCard.CardTags.STARTER_DEFEND);
            onlyBasics &= basic;
            AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
            if (basic && quest instanceof OperationeSolis && ((OperationeSolis)quest).getStage() == 12) {
                basicPlayedInCreation = true;
            }
        }

        public void begin() { current.clear(); open = true; onlyBasics = true; basicPlayedInCreation = false; }

        public boolean isOpen() { return open; }
        public boolean creationTurn() { return open && onlyBasics && basicPlayedInCreation; }

        public boolean finish() {
            if (!open) return false;
            open = false;
            boolean matches = previous != null && previous.equals(current);
            previous = new ArrayList<>(current);
            return matches;
        }
    }

    public static void finishPlayerTurn() {
        if (AbstractDungeon.player == null) return;
        boolean withoutCardBlock = general.SolarCardBlockHistory.finishTurn();
        AbstractPower currentQuest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (currentQuest instanceof OperationeSolis
                && CombatFields.turns.get(AbstractDungeon.player).creationTurn()) {
            ((OperationeSolis)currentQuest).onBasicCardsTurn();
        }
        if (withoutCardBlock && currentQuest instanceof OperationeSolis) {
            ((OperationeSolis)currentQuest).onTurnWithoutCardBlock();
        }
        if (CombatFields.turns.get(AbstractDungeon.player).finish()) {
            AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onMatchingTurns();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class CompletedPlayerTurn {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isWriter() && field.getFieldName().equals("monsterAttacksQueued")) {
                        // This branch runs only after all end-turn actions and queued cards finish.
                        field.replace("{ $proceed($$); if ($1) patches.OperationeSolisPatch.finishPlayerTurn(); }");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class BeginPlayerTurn {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance) {
            CombatFields.turns.get(__instance).begin();
            general.SolarCardBlockHistory.beginTurn();
            AbstractPower quest = __instance.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).beginPlayerTurn();
        }
    }

    public static void shuffled() {
        if (AbstractDungeon.player == null || !CombatFields.turns.get(AbstractDungeon.player).isOpen()) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onShuffle();
    }

    @SpirePatch(clz = com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class CreationEmptyDeckShuffle {
        @SpirePostfixPatch
        public static void postfix() { shuffled(); }
    }

    @SpirePatch(clz = com.megacrit.cardcrawl.actions.common.ShuffleAction.class, method = "update")
    public static class CreationShuffle {
        @SpirePrefixPatch
        public static void prefix(com.megacrit.cardcrawl.actions.common.ShuffleAction __instance, boolean ___triggerRelics) {
            if (!__instance.isDone && ___triggerRelics) shuffled();
        }
    }

    @SpirePatch(clz = PhilosopherStone.class, method = "onEquip")
    public static class PhilosopherStoneEquipped {
        @SpirePostfixPatch
        public static void postfix() { checkTruth(); }
    }

    public static void blockGained(int amount) {
        if (AbstractDungeon.player == null) return;
        general.SolarCardBlockHistory.blockGained(amount);
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onBlockGained(amount);
    }

    public static void intervalEffect(int effect, int actualAmount) {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onIntervalEffect(effect, actualAmount);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard",
            paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
    public static class BeforeCardPlayed {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, AbstractCard c) {
            AbstractPower quest = __instance.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).beforeCardPlayed();
            CombatFields.turns.get(__instance).played(c);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onStageCardPlayed(c);
            if (powers.FaithPower.hasThaumaturgyDamage(c) || powers.FaithPower.hasThaumaturgyBlock(c)) {
                CombatFields.playedThaumaturgy.set(__instance, true);
            }
            if (powers.ElementalDefensePower.hasMagicDamage(c) || powers.ElementalDefensePower.hasMagicBlock(c)) {
                CombatFields.playedMagic.set(__instance, true);
            }
            checkSeparation();
            checkTruth();
        }
    }

    @SpirePatch(clz = Soul.class, method = "shuffle", paramtypez = {AbstractCard.class, boolean.class})
    public static class ExcludeShufflePlacement {
        @SpireRawPatch
        public static void raw(CtBehavior method) throws Exception {
            method.insertBefore("{ patches.OperationeSolisPatch.suppressTopPlacement(); }");
            method.insertAfter("{ patches.OperationeSolisPatch.restoreTopPlacement(); }", true);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class ForeknowledgePowerChange {
        @SpirePostfixPatch
        public static void postfix() { checkForeknowledge(); checkSeparation(); checkAltitude(); checkFortitude(); }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "onEquip")
    public static class FrozenEyeEquipped {
        @SpirePostfixPatch
        public static void postfix(AbstractRelic __instance) {
            if ("Frozen Eye".equals(__instance.relicId)) checkForeknowledge();
            if ("WingedGreaves".equals(__instance.relicId)) checkHermes();
        }
    }

    public static class GenerationHistory {
        private final Set<AbstractCard> originals = Collections.newSetFromMap(new IdentityHashMap<>());
        private final Set<AbstractCard> enteredHand = Collections.newSetFromMap(new IdentityHashMap<>());
        private final Set<AbstractCard.CardColor> colors = EnumSet.noneOf(AbstractCard.CardColor.class);
        private boolean active;

        public void start(AbstractPlayer player) {
            originals.clear();
            enteredHand.clear();
            colors.clear();
            originals.addAll(player.drawPile.group);
            active = true;
        }

        public void enteredHand(AbstractCard card) {
            if (!active || originals.contains(card) || !enteredHand.add(card)) return;
            if (Arrays.asList(AbstractCardEnum.Recluse_COLOR, AbstractCardEnum.Wylder_COLOR,
                    AbstractCardEnum.Guardian_COLOR, AbstractCardEnum.Ironeye_COLOR,
                    AbstractCardEnum.Raider_COLOR, AbstractCardEnum.Duchess_COLOR,
                    AbstractCardEnum.Executor_COLOR, AbstractCardEnum.Revenant_COLOR,
                    AbstractCardEnum.Scholar_COLOR, AbstractCardEnum.Undertaker_COLOR).contains(card.color)) {
                colors.add(card.color);
            }
        }

        public boolean isComplete() {
            return colors.size() == 10;
        }

        public int colorCount() {
            return colors.size();
        }
    }

    public static void recordHand() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) return;
        GenerationHistory history = CombatFields.generated.get(player);
        for (AbstractCard card : player.hand.group) history.enteredHand(card);
        AbstractPower quest = player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).checkGeneratedColors();
    }

    public static void initializedDeck(CardGroup pile) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && pile == player.drawPile) CombatFields.generated.get(player).start(player);
    }

    public static void pilesChanged() {
        if (AbstractDungeon.player == null) return;
        AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
        if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onPilesChanged(false);
    }

    public static void pileChanged(CardGroup pile) {
        if (AbstractDungeon.player != null && pile == AbstractDungeon.player.masterDeck) checkFortitude();
        if (AbstractDungeon.player != null && pile == AbstractDungeon.player.hand) recordHand();
        if (AbstractDungeon.player != null && (pile == AbstractDungeon.player.drawPile
                || pile == AbstractDungeon.player.discardPile)) pilesChanged();
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile", paramtypez = {AbstractCard.class})
    public static class AfterExhaust {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, AbstractCard c) {
            if (AbstractDungeon.player == null) return;
            AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).beforeCardExhausted(c);
        }

        @SpirePostfixPatch
        public static void postfix(CardGroup __instance, AbstractCard c) {
            AbstractPlayer player = AbstractDungeon.player;
            if (player == null || !player.exhaustPile.contains(c)) return;
            AbstractPower quest = player.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onCardExhausted(c);
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class SuccessfulPowerGain {
        @SpireInsertPatch(locator = PlayerDebuffStatsPatch.SuccessLocator.class)
        public static void insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
            if (AbstractDungeon.player != null && __instance.target instanceof AbstractMonster
                    && __instance.amount > 0 && "StunPower".equals(___powerToApply.ID)) {
                AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
                if (quest instanceof OperationeSolis) ((OperationeSolis)quest).onEnemyStunned();
            }
            if (__instance.target != AbstractDungeon.player
                    || AbstractDungeon.player == null) return;
            AbstractPower quest = AbstractDungeon.player.getPower(OperationeSolis.POWER_ID);
            if (quest instanceof OperationeSolis) {
                ((OperationeSolis)quest).onPowerGained(___powerToApply, __instance.amount);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "clear")
    public static class PileMutations {
        @SpireRawPatch
        public static void raw(CtBehavior behavior) throws Exception {
            for (CtMethod method : behavior.getDeclaringClass().getDeclaredMethods()) {
                String name = method.getName();
                if (name.equals("initializeDeck")) {
                    method.insertBefore("{ patches.OperationeSolisPatch.suppressTopPlacement(); }");
                    method.insertAfter("{ patches.OperationeSolisPatch.initializedDeck(this); }");
                    method.insertAfter("{ patches.OperationeSolisPatch.restoreTopPlacement(); }", true);
                }
                if (name.equals("addToTop")) {
                    method.insertAfter("{ patches.OperationeSolisPatch.recordTopPlacement(this); }");
                }
                if (name.equals("clear") || name.equals("removeCard") || name.equals("removeTopCard")
                        || name.equals("addToTop") || name.equals("addToBottom") || name.equals("addToRandomSpot") || name.equals("addToHand")
                        || name.equals("shuffle") || name.equals("initializeDeck") || name.startsWith("sort")
                        || name.startsWith("moveTo") || name.equals("resetCardBeforeMoving")) {
                    method.insertAfter("{ patches.OperationeSolisPatch.pileChanged(this); }");
                }
            }
        }
    }

    @SpirePatch(clz = general.SmithingBody.class, method = "updateAction")
    public static class AfterAction {
        @SpirePostfixPatch
        public static void postfix() {
            recordHand();
            pilesChanged();
            checkTruth();
            checkForeknowledge();
            checkSeparation();
            checkAltitude();
            checkGlory();
            checkObstacles();
            checkFortitude();
            checkHermes();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "upgradeName")
    public static class AfterUpgrade {
        @SpirePostfixPatch
        public static void postfix() {
            pilesChanged();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class CombatFields {
        public static SpireField<Boolean> completed = new SpireField<>(() -> false);
        public static SpireField<GenerationHistory> generated = new SpireField<>(GenerationHistory::new);
        public static SpireField<Boolean> placedOnTop = new SpireField<>(() -> false);
        public static SpireField<Boolean> playedThaumaturgy = new SpireField<>(() -> false);
        public static SpireField<Boolean> playedMagic = new SpireField<>(() -> false);
        public static SpireField<TurnHistory> turns = new SpireField<>(TurnHistory::new);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class ResetCompletion {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance) {
            CombatFields.completed.set(__instance, false);
            CombatFields.generated.set(__instance, new GenerationHistory());
            CombatFields.placedOnTop.set(__instance, false);
            CombatFields.playedThaumaturgy.set(__instance, false);
            CombatFields.playedMagic.set(__instance, false);
            CombatFields.turns.set(__instance, new TurnHistory());
            topPlacementSuppression = 0;
            general.PotionTaskHistory.reset();
            general.SolarAttackHistory.reset();
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class SilentApplication {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(ApplyPowerAction __instance,
                                               AbstractPower ___powerToApply, AbstractCreature ___target) {
            if (___powerToApply instanceof OperationeSolis) {
                if (___target instanceof AbstractPlayer) {
                    OperationeSolis.obtain((AbstractPlayer)___target);
                }
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    public static ArrayList<AbstractPower> visiblePowers(ArrayList<AbstractPower> powers) {
        ArrayList<AbstractPower> visible = new ArrayList<>();
        for (AbstractPower power : powers) {
            if (!(power instanceof OperationeSolis)) {
                visible.add(power);
            }
        }
        return visible;
    }

    @SpirePatches({
            @SpirePatch(clz = AbstractCreature.class, method = "renderPowerIcons"),
            @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
    })
    public static class HidePresentation {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess field) throws CannotCompileException {
                    if (field.isReader() && field.getFieldName().equals("powers")) {
                        // Filter only the render view; never mutate the live power list.
                        field.replace("$_ = patches.OperationeSolisPatch.visiblePowers($proceed());");
                    }
                }
            };
        }
    }
}
