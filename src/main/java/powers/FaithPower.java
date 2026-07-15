package powers;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

public class FaithPower extends AbstractPower {
    public static final String POWER_ID = "FaithPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static Set<String> englishThaumaturgyCardIds;

    public FaithPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        if (this.amount >= 999) {
            this.amount = 999;
        }
        if (this.amount <= -999) {
            this.amount = -999;
        }
        updateDescription();
        PowerIconHelper.load(this, POWER_ID);
        this.canGoNegative = true;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        if (this.amount <= -999) {
            this.amount = -999;
        }
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount == 0) {
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        if (this.amount <= -999) {
            this.amount = -999;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
            this.type = PowerType.BUFF;
        } else {
            int tmp = -this.amount;
            this.description = DESCRIPTIONS[1] + tmp + DESCRIPTIONS[2];
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type == DamageInfo.DamageType.NORMAL && isThaumaturgyCard(card)) {
            return damage + this.amount;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (isThaumaturgyCard(card)) {
            blockAmount += this.amount;
            if (blockAmount < 0.0F) {
                return 0.0F;
            }
        }
        return blockAmount;
    }

    public static void applyMagicNumber(AbstractCard card) {
        if (AbstractDungeon.player == null || card.baseMagicNumber < 0 || !isThaumaturgyCard(card)) {
            return;
        }

        int value = card.baseMagicNumber;
        AbstractPower intelligencePower = AbstractDungeon.player.getPower(IntelligencePower.POWER_ID);
        if (ElementalDefensePower.isMagicCard(card) && intelligencePower != null) {
            value += intelligencePower.amount;
        }

        AbstractPower faithPower = AbstractDungeon.player.getPower(POWER_ID);
        if (faithPower != null) {
            value += faithPower.amount;
        }
        if (value < 0) {
            value = 0;
        }
        card.magicNumber = value;
        card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
    }

    public static boolean isThaumaturgyCard(AbstractCard card) {
        if (card == null) {
            return false;
        }
        if (card.rawDescription != null && containsThaumaturgyText(card.rawDescription)) {
            return true;
        }
        return getEnglishThaumaturgyCardIds().contains(card.cardID);
    }

    private static boolean containsThaumaturgyText(String text) {
        String lowerText = text.toLowerCase(Locale.ROOT);
        return lowerText.contains("thaumaturgy") || text.contains("濂囨湳");
    }

    private static Set<String> getEnglishThaumaturgyCardIds() {
        if (englishThaumaturgyCardIds == null) {
            englishThaumaturgyCardIds = new HashSet<>();
            try {
                String json = Gdx.files.internal("localization/ERNMod_cards-eng.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
                JsonObject root = new JsonParser().parse(json).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    JsonObject cardStrings = entry.getValue().getAsJsonObject();
                    JsonElement description = cardStrings.get("DESCRIPTION");
                    if (description != null && containsThaumaturgyText(description.getAsString())) {
                        englishThaumaturgyCardIds.add(entry.getKey());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return englishThaumaturgyCardIds;
    }
}

