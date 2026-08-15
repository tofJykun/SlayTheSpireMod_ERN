package powers;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ElementalDefensePower extends AbstractPower {
    public static final String POWER_ID = "ElementalDefensePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static Set<String> englishMagicCardIds;
    private static Set<String> englishMagicDamageCardIds;
    private static Set<String> englishMagicBlockCardIds;
    private final Set<UUID> reducedCardUuids = new HashSet<>();

    public ElementalDefensePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        reduceCombatCardsForCurrentAmount();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        reduceHandCardsForCurrentAmount();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        reduceCombatCardsBy(stackAmount);
        updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        reduceCardForCurrentAmount(card);
    }

    @Override
    public void onDrawOrDiscard() {
        reduceHandCardsForCurrentAmount();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    private void reduceCombatCardsForCurrentAmount() {
        if (AbstractDungeon.player == null) {
            return;
        }
        reduceGroupForCurrentAmount(AbstractDungeon.player.hand);
        reduceGroupForCurrentAmount(AbstractDungeon.player.drawPile);
        reduceGroupForCurrentAmount(AbstractDungeon.player.discardPile);
        reduceGroupForCurrentAmount(AbstractDungeon.player.exhaustPile);
    }

    private void reduceCombatCardsBy(int reduction) {
        if (AbstractDungeon.player == null) {
            return;
        }
        reduceGroupBy(AbstractDungeon.player.hand, reduction);
        reduceGroupBy(AbstractDungeon.player.drawPile, reduction);
        reduceGroupBy(AbstractDungeon.player.discardPile, reduction);
        reduceGroupBy(AbstractDungeon.player.exhaustPile, reduction);
    }

    private void reduceHandCardsForCurrentAmount() {
        if (AbstractDungeon.player == null) {
            return;
        }
        reduceGroupForCurrentAmount(AbstractDungeon.player.hand);
    }

    private void reduceGroupForCurrentAmount(CardGroup group) {
        for (AbstractCard card : group.group) {
            reduceCardForCurrentAmount(card);
        }
    }

    private void reduceGroupBy(CardGroup group, int reduction) {
        for (AbstractCard card : group.group) {
            reduceCardBy(card, reduction);
        }
    }

    private void reduceCardForCurrentAmount(AbstractCard card) {
        if (!this.reducedCardUuids.contains(card.uuid)) {
            reduceCardBy(card, this.amount);
        }
    }

    private void reduceCardBy(AbstractCard card, int reduction) {
        if (reduction > 0 && card.cost >= 0 && isMagicCard(card)) {
            card.modifyCostForCombat(-reduction);
            this.reducedCardUuids.add(card.uuid);
        }
    }

    public static boolean isMagicCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.rawDescription != null
                && containsMagicText(card.rawDescription)) {
            return true;
        }
        return getEnglishMagicCardIds().contains(card.cardID);
    }

    public static boolean hasMagicDamage(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.rawDescription != null && containsMagicDamageText(card.rawDescription)) {
            return true;
        }
        return getEnglishMagicDamageCardIds().contains(card.cardID);
    }

    public static boolean hasMagicBlock(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.rawDescription != null && containsMagicBlockText(card.rawDescription)) {
            return true;
        }
        return getEnglishMagicBlockCardIds().contains(card.cardID);
    }

    private static boolean containsMagicText(String text) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        return lowerText.contains("magic") || text.contains("魔法") || text.contains("榄旀硶");
    }

    private static boolean containsMagicDamageText(String text) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        return lowerText.contains("magic damage") || text.contains("魔法伤害") || text.contains("榄旀硶浼ゅ害");
    }

    private static boolean containsMagicBlockText(String text) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        return lowerText.contains("magic block") || text.contains("魔法格挡") || text.contains("魔法 格挡")
                || text.contains("榄旀硶鏍兼尅") || text.contains("榄旀硶 鏍兼尅");
    }

    private static Set<String> getEnglishMagicCardIds() {
        if (englishMagicCardIds == null) {
            englishMagicCardIds = new HashSet<>();
            try {
                String json = Gdx.files.internal("localization/ERNMod_cards-eng.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
                JsonObject root = new JsonParser().parse(json).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    JsonObject cardStrings = entry.getValue().getAsJsonObject();
                    JsonElement description = cardStrings.get("DESCRIPTION");
                    if (description != null && containsMagicText(description.getAsString())) {
                        englishMagicCardIds.add(entry.getKey());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return englishMagicCardIds;
    }

    private static Set<String> getEnglishMagicDamageCardIds() {
        if (englishMagicDamageCardIds == null) {
            englishMagicDamageCardIds = new HashSet<>();
            try {
                String json = Gdx.files.internal("localization/ERNMod_cards-eng.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
                JsonObject root = new JsonParser().parse(json).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    JsonObject cardStrings = entry.getValue().getAsJsonObject();
                    JsonElement description = cardStrings.get("DESCRIPTION");
                    if (description != null && containsMagicDamageText(description.getAsString())) {
                        englishMagicDamageCardIds.add(entry.getKey());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return englishMagicDamageCardIds;
    }

    private static Set<String> getEnglishMagicBlockCardIds() {
        if (englishMagicBlockCardIds == null) {
            englishMagicBlockCardIds = new HashSet<>();
            try {
                String json = Gdx.files.internal("localization/ERNMod_cards-eng.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
                JsonObject root = new JsonParser().parse(json).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    JsonObject cardStrings = entry.getValue().getAsJsonObject();
                    JsonElement description = cardStrings.get("DESCRIPTION");
                    if (description != null && containsMagicBlockText(description.getAsString())) {
                        englishMagicBlockCardIds.add(entry.getKey());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return englishMagicBlockCardIds;
    }
}

