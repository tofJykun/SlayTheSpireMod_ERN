package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.Map;

public class ArcanePower extends AbstractPower {
    public static final String POWER_ID = "ArcanePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int CHANCE_PER_STACK = 5;

    public ArcanePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + getChancePercent(this.amount) + DESCRIPTIONS[1];
    }

    public static void tryAddRareCard(ArrayList<AbstractCard> cards) {
        if (cards == null || cards.isEmpty() || AbstractDungeon.player == null
                || !AbstractDungeon.player.hasPower(POWER_ID) || hasRare(cards)) {
            return;
        }

        AbstractPower power = AbstractDungeon.player.getPower(POWER_ID);
        int chance = getChancePercent(power.amount);
        if (chance <= 0 || AbstractDungeon.cardRandomRng.random(99) >= chance) {
            return;
        }

        AbstractCard rare = getRandomRareReward(cards);
        if (rare == null) {
            return;
        }

        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.onPreviewObtainCard(rare);
        }
        cards.add(rare);
        power.flash();
    }

    private static boolean hasRare(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card != null && card.rarity == AbstractCard.CardRarity.RARE) {
                return true;
            }
        }
        return false;
    }

    private static AbstractCard getRandomRareReward(ArrayList<AbstractCard> existingCards) {
        AbstractCard card = null;
        int attempts = 0;
        while (attempts < 20) {
            card = getRareCardFromRewardPool();
            if (card == null || !containsCard(existingCards, card.cardID)) {
                break;
            }
            attempts++;
        }
        return card == null ? null : card.makeCopy();
    }

    private static AbstractCard getRareCardFromRewardPool() {
        if (AbstractDungeon.player.hasRelic("PrismaticShard")) {
            return getAnyColorRareCard();
        }
        return AbstractDungeon.getCard(AbstractCard.CardRarity.RARE, AbstractDungeon.cardRandomRng);
    }

    private static AbstractCard getAnyColorRareCard() {
        CardGroup anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet()) {
            AbstractCard card = entry.getValue();
            if (card.rarity == AbstractCard.CardRarity.RARE
                    && card.type != AbstractCard.CardType.CURSE
                    && card.type != AbstractCard.CardType.STATUS
                    && (!UnlockTracker.isCardLocked(entry.getKey()) || Settings.treatEverythingAsUnlocked())) {
                anyCard.addToBottom(card);
            }
        }
        if (anyCard.isEmpty()) {
            return null;
        }
        return anyCard.getRandomCard(AbstractDungeon.cardRandomRng, AbstractCard.CardRarity.RARE);
    }

    private static boolean containsCard(ArrayList<AbstractCard> cards, String cardID) {
        for (AbstractCard card : cards) {
            if (card != null && card.cardID.equals(cardID)) {
                return true;
            }
        }
        return false;
    }

    private static int getChancePercent(int amount) {
        return Math.min(Math.max(amount, 0) * CHANCE_PER_STACK, 100);
    }
}
