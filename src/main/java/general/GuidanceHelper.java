package general;

import cards.recluse.ElementalDefense;
import cards.recluse.Ictarus;
import cards.recluse.LingeringDragoncrest;
import cards.duchess.WaterfowlDance;
import cards.wylder.OnslaughtStake;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.ERNModClassEnum;

import java.util.ArrayList;
import java.util.Locale;

public final class GuidanceHelper {
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("SeekGuidance");
    private static final int HIGH_ATTACK_DAMAGE = 30;
    private static final String LONG_FIGHT_MARKER = "__ERN_LONG_FIGHT_GUIDANCE__";

    private GuidanceHelper() {
    }

    public static String randomMessage() {
        ArrayList<String> candidates = collectMessages();
        if (candidates.isEmpty()) {
            return UI_STRINGS.TEXT[15];
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(candidates.size() - 1);
        String message = candidates.get(index);
        return LONG_FIGHT_MARKER.equals(message) ? longFightMessage() : message;
    }

    private static ArrayList<String> collectMessages() {
        ArrayList<String> messages = new ArrayList<>();
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            messages.add(UI_STRINGS.TEXT[15]);
            return messages;
        }

        addCommonMessages(messages, player);
        if (isWylderDuchessOrRecluse(player)) {
            addLimitedCharacterMessages(messages, player);
        } else {
            messages.add(UI_STRINGS.TEXT[0]);
        }
        return messages;
    }

    private static void addCommonMessages(ArrayList<String> messages, AbstractPlayer player) {
        if (potionSlotsFull(player)) {
            messages.add(UI_STRINGS.TEXT[1]);
        }
        if (GuidanceStats.getPotionsUsedThisCombat() >= 2
                || (GuidanceStats.getPotionsUsedThisCombat() > 0 && !player.hasAnyPotions())) {
            messages.add(UI_STRINGS.TEXT[20]);
        }
        if (GameActionManager.turn <= 2 && GuidanceStats.getShufflesThisCombat() > 0) {
            messages.add(UI_STRINGS.TEXT[21]);
        }
        if (GuidanceStats.hasPlayedXCostCardThisTurn() && player.hasRelic("Chemical X")) {
            messages.add(UI_STRINGS.TEXT[22]);
        }
        if (isDonuDecaBossFight() && GuidanceStats.hasDonuDiedThisTurn()) {
            messages.add(UI_STRINGS.TEXT[23]);
        }
        if (isDonuDecaBossFight() && GuidanceStats.hasDecaDiedThisTurn()) {
            messages.add(UI_STRINGS.TEXT[24]);
        }
        if (isAwakenedOneFightWithDeadCultists()) {
            messages.add(UI_STRINGS.TEXT[25]);
        }
        if (hasCreatureAtOneOrTwoHp(player)) {
            messages.add(UI_STRINGS.TEXT[26]);
        }
        if (shouldSayGoodbyeWorld(player)) {
            messages.add(UI_STRINGS.TEXT[27]);
        }
        if (GuidanceStats.getLastTurnHpLoss() >= 6) {
            messages.add(UI_STRINGS.TEXT[2]);
        }
        if (GuidanceStats.getMaxAttackCardDamageThisTurn() > HIGH_ATTACK_DAMAGE) {
            messages.add(UI_STRINGS.TEXT[3]);
            messages.add(UI_STRINGS.TEXT[6]);
        }
        boolean enemiesAttack = enemiesWillAttack();
        if (enemiesAttack && defensiveOrDrawCardsInHand(player) <= 1) {
            messages.add(UI_STRINGS.TEXT[4]);
            messages.add(UI_STRINGS.TEXT[5]);
        }
        if (!enemiesAttack && offensiveOrDebuffCardsInHand(player) <= 1) {
            messages.add(UI_STRINGS.TEXT[4]);
            messages.add(UI_STRINGS.TEXT[7]);
        }
        if (isActThreeBossWithoutTimeEater()) {
            messages.add(UI_STRINGS.TEXT[8]);
            messages.add(UI_STRINGS.TEXT[28]);
        }
        if (isActThreeBossWithoutAwakenedOne()) {
            messages.add(UI_STRINGS.TEXT[29]);
        }
        if (isTimeEaterBossFight()) {
            messages.add(String.format(UI_STRINGS.TEXT[30], player.getLocalizedCharacterName()));
        }
        messages.add(UI_STRINGS.TEXT[31]);
        if (GameActionManager.turn >= 10 && hasLivingEnemies()) {
            messages.add(LONG_FIGHT_MARKER);
        }
        if (isChampBossFight()) {
            messages.add(UI_STRINGS.TEXT[33]);
        }
        if (isAwakenedOneSecondPhaseWithCultists()) {
            messages.add(UI_STRINGS.TEXT[9]);
        }
        if (CardCrawlGame.playtime < AbstractDungeon.floorNum * 20.0F) {
            messages.add(UI_STRINGS.TEXT[10]);
        }
        if (handCost(player) < player.energy.energy) {
            messages.add(UI_STRINGS.TEXT[11]);
        }
        int cardsPlayed = cardsPlayedThisCombat();
        if (GuidanceStats.getTotalBlockThisCombat() < cardsPlayed * 2) {
            messages.add(UI_STRINGS.TEXT[12]);
        }
        if (GuidanceStats.getTotalDamageThisCombat() + GuidanceStats.getTotalBlockThisCombat() < cardsPlayed * 6) {
            messages.add(UI_STRINGS.TEXT[13]);
        }
        AbstractMonster highestAttacker = highestDamageIntentMonster();
        if (highestAttacker != null && enemyDamageThresholdExceeded()) {
            messages.add(UI_STRINGS.TEXT[14] + highestAttacker.name);
        }
        if (player.hasRelic("Philosopher's Stone") && isThreeByrds()) {
            messages.add(UI_STRINGS.TEXT[16]);
        }
        messages.add(UI_STRINGS.TEXT[15]);
        messages.add(UI_STRINGS.TEXT[17]);
    }

    private static void addLimitedCharacterMessages(ArrayList<String> messages, AbstractPlayer player) {
        if (matchesBullyEnemies(player)) {
            messages.add(UI_STRINGS.TEXT[18]);
        }
        if (matchesWinningDeck(player)) {
            messages.add(UI_STRINGS.TEXT[19]);
        }
    }

    private static boolean potionSlotsFull(AbstractPlayer player) {
        for (com.megacrit.cardcrawl.potions.AbstractPotion potion : player.potions) {
            if ("Potion Slot".equals(potion.ID)) {
                return false;
            }
        }
        return !player.potions.isEmpty();
    }

    private static boolean enemiesWillAttack() {
        return enemyIntentTotalDamage() > 0;
    }

    private static boolean enemyDamageThresholdExceeded() {
        int threshold;
        if ("Exordium".equals(AbstractDungeon.id)) {
            threshold = 20;
        } else if ("TheCity".equals(AbstractDungeon.id)) {
            threshold = 25;
        } else if ("TheBeyond".equals(AbstractDungeon.id)) {
            threshold = 35;
        } else if ("TheEnding".equals(AbstractDungeon.id)) {
            threshold = 40;
        } else {
            threshold = 35;
        }
        return enemyIntentTotalDamage() > threshold;
    }

    public static int enemyIntentTotalDamage() {
        int total = 0;
        if (AbstractDungeon.getMonsters() == null) {
            return total;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped() || !isAttackIntent(monster.intent)) {
                continue;
            }
            total += intentDamage(monster);
        }
        return total;
    }

    private static AbstractMonster highestDamageIntentMonster() {
        AbstractMonster highest = null;
        int highestDamage = 0;
        if (AbstractDungeon.getMonsters() == null) {
            return null;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped() || !isAttackIntent(monster.intent)) {
                continue;
            }
            int damage = intentDamage(monster);
            if (damage > highestDamage) {
                highestDamage = damage;
                highest = monster;
            }
        }
        return highest;
    }

    private static int intentDamage(AbstractMonster monster) {
        int damage = monster.getIntentDmg();
        if (damage <= 0) {
            return 0;
        }
        try {
            boolean isMulti = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "isMultiDmg");
            if (isMulti) {
                Integer multi = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentMultiAmt");
                return damage * Math.max(1, multi.intValue());
            }
        } catch (Exception ignored) {
        }
        return damage;
    }

    private static boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    private static int defensiveOrDrawCardsInHand(AbstractPlayer player) {
        int count = 0;
        for (AbstractCard card : player.hand.group) {
            if (card != null && (card.baseBlock > 0 || textContains(card, "draw", "抽"))) {
                count++;
            }
        }
        return count;
    }

    private static boolean shouldSayGoodbyeWorld(AbstractPlayer player) {
        return player.currentHealth <= 3 || (drawCardsInHand(player) == 0 && blockInHand(player) < enemyIntentTotalDamage());
    }

    private static int drawCardsInHand(AbstractPlayer player) {
        int count = 0;
        for (AbstractCard card : player.hand.group) {
            if (card != null && textContains(card, "draw", "抽")) {
                count++;
            }
        }
        return count;
    }

    private static int blockInHand(AbstractPlayer player) {
        int total = 0;
        for (AbstractCard card : player.hand.group) {
            if (card != null && card.baseBlock >= 0) {
                total += Math.max(0, card.block >= 0 ? card.block : card.baseBlock);
            }
        }
        return total;
    }

    private static int offensiveOrDebuffCardsInHand(AbstractPlayer player) {
        int count = 0;
        for (AbstractCard card : player.hand.group) {
            if (card != null && (card.baseDamage > 0 || card.type == AbstractCard.CardType.ATTACK
                    || textContains(card, "apply", "vulnerable", "weak", "poison", "frostbite", "sleep",
                    "madness", "bloodloss", "bloodburn", "scarlet rot", "poise break", "给予", "失去",
                    "易伤", "虚弱", "中毒", "冻伤", "睡眠", "发狂", "出血", "燃命", "猩红腐败", "削韧"))) {
                count++;
            }
        }
        return count;
    }

    private static boolean textContains(AbstractCard card, String... needles) {
        String text = ((card.rawDescription == null ? "" : card.rawDescription) + " "
                + (card.description == null ? "" : card.description)).toLowerCase(Locale.ROOT);
        for (String needle : needles) {
            if (text.contains(needle.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private static int handCost(AbstractPlayer player) {
        int total = 0;
        for (AbstractCard card : player.hand.group) {
            if (card != null && card.costForTurn > 0) {
                total += card.costForTurn;
            }
        }
        return total;
    }

    private static boolean isActThreeBossWithoutTimeEater() {
        return "TheBeyond".equals(AbstractDungeon.id)
                && AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss
                && !"Time Eater".equals(AbstractDungeon.bossKey);
    }

    private static boolean isActThreeBossWithoutAwakenedOne() {
        return "TheBeyond".equals(AbstractDungeon.id)
                && AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss
                && !"Awakened One".equals(AbstractDungeon.bossKey);
    }

    private static boolean isTimeEaterBossFight() {
        return "TheBeyond".equals(AbstractDungeon.id)
                && AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss
                && hasAnyEnemyIncludingDead("TimeEater");
    }

    private static boolean isChampBossFight() {
        return "TheCity".equals(AbstractDungeon.id)
                && AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss
                && hasAnyEnemyIncludingDead("Champ");
    }

    private static boolean isAwakenedOneSecondPhaseWithCultists() {
        boolean awakenedSecondPhase = false;
        int livingCultists = 0;
        if (AbstractDungeon.getMonsters() == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            if ("AwakenedOne".equals(monster.id)) {
                try {
                    Boolean form1 = ReflectionHacks.getPrivate(monster, monster.getClass(), "form1");
                    awakenedSecondPhase = form1 != null && !form1.booleanValue();
                } catch (Exception ignored) {
                    awakenedSecondPhase = false;
                }
            } else if ("Cultist".equals(monster.id)) {
                livingCultists++;
            }
        }
        return awakenedSecondPhase && livingCultists >= 2;
    }

    private static boolean isDonuDecaBossFight() {
        if (!isActThreeBossRoom() || AbstractDungeon.getMonsters() == null) {
            return false;
        }
        boolean hasDonu = false;
        boolean hasDeca = false;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null) {
                continue;
            }
            if ("Donu".equals(monster.id)) {
                hasDonu = true;
            } else if ("Deca".equals(monster.id)) {
                hasDeca = true;
            }
        }
        return hasDonu && hasDeca;
    }

    private static boolean isAwakenedOneFightWithDeadCultists() {
        if (!isActThreeBossRoom() || AbstractDungeon.getMonsters() == null) {
            return false;
        }
        boolean hasAwakenedOne = false;
        int cultists = 0;
        int deadCultists = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null) {
                continue;
            }
            if ("AwakenedOne".equals(monster.id)) {
                hasAwakenedOne = true;
            } else if ("Cultist".equals(monster.id)) {
                cultists++;
                if (monster.isDeadOrEscaped() || monster.isDying || monster.currentHealth <= 0) {
                    deadCultists++;
                }
            }
        }
        return hasAwakenedOne && cultists >= 2 && deadCultists >= 2;
    }

    private static boolean isActThreeBossRoom() {
        return "TheBeyond".equals(AbstractDungeon.id)
                && AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
    }

    private static boolean isThreeByrds() {
        if (AbstractDungeon.getMonsters() == null || AbstractDungeon.getMonsters().monsters.size() != 3) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || !"Byrd".equals(monster.id)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasCreatureAtOneOrTwoHp(AbstractPlayer player) {
        if (player.currentHealth == 1 || player.currentHealth == 2) {
            return true;
        }
        if (AbstractDungeon.getMonsters() == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            if (monster.currentHealth == 1 || monster.currentHealth == 2) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWylderDuchessOrRecluse(AbstractPlayer player) {
        return player.chosenClass == ERNModClassEnum.Wylder_CLASS
                || player.chosenClass == ERNModClassEnum.Duchess_CLASS
                || player.chosenClass == ERNModClassEnum.Recluse_CLASS;
    }

    private static boolean matchesBullyEnemies(AbstractPlayer player) {
        if (player.chosenClass == ERNModClassEnum.Wylder_CLASS) {
            return hasEnemy("GremlinNob", "Chosen", "SlaverBoss");
        }
        if (player.chosenClass == ERNModClassEnum.Recluse_CLASS) {
            return hasEnemy("GremlinNob", "AwakenedOne", "TimeEater", "Chosen");
        }
        if (player.chosenClass == ERNModClassEnum.Duchess_CLASS) {
            return hasEnemy("TimeEater", "SlaverBoss");
        }
        return false;
    }

    private static boolean hasEnemy(String... ids) {
        if (AbstractDungeon.getMonsters() == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null || monster.isDeadOrEscaped()) {
                continue;
            }
            for (String id : ids) {
                if (id.equals(monster.id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasAnyEnemyIncludingDead(String... ids) {
        if (AbstractDungeon.getMonsters() == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == null) {
                continue;
            }
            for (String id : ids) {
                if (id.equals(monster.id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasLivingEnemies() {
        if (AbstractDungeon.getMonsters() == null) {
            return false;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                return true;
            }
        }
        return false;
    }

    private static String longFightMessage() {
        ArrayList<AbstractMonster> livingEnemies = new ArrayList<>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster != null && !monster.isDeadOrEscaped()) {
                    livingEnemies.add(monster);
                }
            }
        }
        if (livingEnemies.isEmpty()) {
            return UI_STRINGS.TEXT[31];
        }
        AbstractMonster monster = livingEnemies.get(BulkPotionQueue.randomIndex(livingEnemies.size()));
        return String.format(UI_STRINGS.TEXT[32], monster.name);
    }

    private static boolean matchesWinningDeck(AbstractPlayer player) {
        if (player.chosenClass == ERNModClassEnum.Recluse_CLASS) {
            return deckHas(Ictarus.ID, ElementalDefense.ID, LingeringDragoncrest.ID);
        }
        if (player.chosenClass == ERNModClassEnum.Duchess_CLASS) {
            return deckHas(WaterfowlDance.ID);
        }
        if (player.chosenClass == ERNModClassEnum.Wylder_CLASS) {
            return deckHas(OnslaughtStake.ID);
        }
        return false;
    }

    private static boolean deckHas(String... cardIds) {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return false;
        }
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card == null) {
                continue;
            }
            for (String id : cardIds) {
                if (id.equals(card.cardID)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int cardsPlayedThisCombat() {
        return AbstractDungeon.actionManager == null ? 0 : AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
    }
}
