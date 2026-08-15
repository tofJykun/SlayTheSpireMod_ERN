package general;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class GuidanceStats {
    private static int lastTurnHpLoss;
    private static int currentTurnHpLoss;
    private static int maxAttackCardDamageThisTurn;
    private static int currentAttackCardDamage;
    private static AbstractCard currentAttackCard;
    private static int totalDamageThisCombat;
    private static int totalBlockThisCombat;
    private static int potionsUsedThisCombat;
    private static int shufflesThisCombat;
    private static boolean xCostCardPlayedThisTurn;
    private static boolean donuDiedThisTurn;
    private static boolean decaDiedThisTurn;

    private GuidanceStats() {
    }

    public static void resetCombat() {
        lastTurnHpLoss = 0;
        currentTurnHpLoss = 0;
        maxAttackCardDamageThisTurn = 0;
        currentAttackCardDamage = 0;
        currentAttackCard = null;
        totalDamageThisCombat = 0;
        totalBlockThisCombat = 0;
        potionsUsedThisCombat = 0;
        shufflesThisCombat = 0;
        xCostCardPlayedThisTurn = false;
        donuDiedThisTurn = false;
        decaDiedThisTurn = false;
    }

    public static void startPlayerTurn() {
        lastTurnHpLoss = currentTurnHpLoss;
        currentTurnHpLoss = 0;
        maxAttackCardDamageThisTurn = 0;
        currentAttackCardDamage = 0;
        currentAttackCard = null;
        xCostCardPlayedThisTurn = false;
        donuDiedThisTurn = false;
        decaDiedThisTurn = false;
    }

    public static void recordDamage(AbstractCreature target, DamageInfo info, int damage) {
        if (damage <= 0 || info == null) {
            return;
        }
        if (target instanceof AbstractPlayer) {
            currentTurnHpLoss += damage;
        }
        if (target instanceof AbstractMonster && info.owner instanceof AbstractPlayer) {
            totalDamageThisCombat += damage;
            AbstractCard card = currentCard();
            if (card != null && card.type == AbstractCard.CardType.ATTACK) {
                if (card != currentAttackCard) {
                    currentAttackCard = card;
                    currentAttackCardDamage = 0;
                }
                currentAttackCardDamage += damage;
                maxAttackCardDamageThisTurn = Math.max(maxAttackCardDamageThisTurn, currentAttackCardDamage);
            }
        }
    }

    public static void recordBlockGain(AbstractCreature target, int beforeBlock, int afterBlock) {
        if (target instanceof AbstractPlayer && afterBlock > beforeBlock) {
            totalBlockThisCombat += afterBlock - beforeBlock;
        }
    }

    public static void recordPotionUse() {
        potionsUsedThisCombat++;
    }

    public static void recordShuffle() {
        shufflesThisCombat++;
    }

    public static void recordCardUse(AbstractCard card) {
        if (card != null && card.cost == -1) {
            xCostCardPlayedThisTurn = true;
        }
    }

    public static void recordMonsterDeath(AbstractMonster monster) {
        if (monster == null) {
            return;
        }
        if ("Donu".equals(monster.id)) {
            donuDiedThisTurn = true;
        } else if ("Deca".equals(monster.id)) {
            decaDiedThisTurn = true;
        }
    }

    public static int getLastTurnHpLoss() {
        return lastTurnHpLoss;
    }

    public static int getMaxAttackCardDamageThisTurn() {
        return maxAttackCardDamageThisTurn;
    }

    public static int getTotalDamageThisCombat() {
        return totalDamageThisCombat;
    }

    public static int getTotalBlockThisCombat() {
        return totalBlockThisCombat;
    }

    public static int getPotionsUsedThisCombat() {
        return potionsUsedThisCombat;
    }

    public static int getShufflesThisCombat() {
        return shufflesThisCombat;
    }

    public static boolean hasPlayedXCostCardThisTurn() {
        return xCostCardPlayedThisTurn;
    }

    public static boolean hasDonuDiedThisTurn() {
        return donuDiedThisTurn;
    }

    public static boolean hasDecaDiedThisTurn() {
        return decaDiedThisTurn;
    }

    private static AbstractCard currentCard() {
        if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager == null) {
            return null;
        }
        return com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()
                ? null
                : com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisCombat.get(
                com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);
    }
}
