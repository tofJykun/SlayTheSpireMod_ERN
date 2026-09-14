package general;

import cards.raider.BleedStone;
import cards.raider.Boltstone;
import cards.raider.DarknightStone;
import cards.raider.DemonTitanite;
import cards.raider.DragonScale;
import cards.raider.Faintstone;
import cards.raider.FiredrakeStone;
import cards.raider.MagicStone;
import cards.raider.MurkyHandScythe;
import cards.raider.OldMundaneStone;
import cards.raider.Palestone;
import cards.raider.PoisonStone;
import cards.raider.RawStone;
import cards.raider.TitaniteChunk;
import cards.raider.TitaniteSlab;
import cards.raider.TitaniteShard;
import cards.raider.TwinklingTitanite;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.ReplayField;
import relics.LargeEmber;
import powers.MoonOfNokstellaPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class SmithingHelper {
    private static AbstractCard activeCraftmanCreation;
    public static final String TITANITE_SHARD_ID = TitaniteShard.ID;
    public static final String TITANITE_CHUNK_ID = TitaniteChunk.ID;
    public static final String TITANITE_SLAB_ID = TitaniteSlab.ID;
    public static final String TWINKLING_TITANITE_ID = TwinklingTitanite.ID;
    public static final String DEMON_TITANITE_ID = DemonTitanite.ID;
    public static final String DRAGON_SCALE_ID = DragonScale.ID;
    public static final String MURKY_HAND_SCYTHE_ID = MurkyHandScythe.ID;

    private static final ArrayList<String> INFUSION_MATERIAL_IDS = new ArrayList<>();
    private static final ArrayList<String> REINFORCEMENT_MATERIAL_IDS = new ArrayList<>();

    static {
        INFUSION_MATERIAL_IDS.add(RawStone.ID);
        INFUSION_MATERIAL_IDS.add(Faintstone.ID);
        INFUSION_MATERIAL_IDS.add(FiredrakeStone.ID);
        INFUSION_MATERIAL_IDS.add(Boltstone.ID);
        INFUSION_MATERIAL_IDS.add(DarknightStone.ID);
        INFUSION_MATERIAL_IDS.add(PoisonStone.ID);
        INFUSION_MATERIAL_IDS.add(BleedStone.ID);
        INFUSION_MATERIAL_IDS.add(MagicStone.ID);
        INFUSION_MATERIAL_IDS.add(OldMundaneStone.ID);
        INFUSION_MATERIAL_IDS.add(Palestone.ID);

        REINFORCEMENT_MATERIAL_IDS.add(TITANITE_SHARD_ID);
        REINFORCEMENT_MATERIAL_IDS.add(TITANITE_CHUNK_ID);
        REINFORCEMENT_MATERIAL_IDS.add(TITANITE_SLAB_ID);
        REINFORCEMENT_MATERIAL_IDS.add(TWINKLING_TITANITE_ID);
        REINFORCEMENT_MATERIAL_IDS.add(DEMON_TITANITE_ID);
        REINFORCEMENT_MATERIAL_IDS.add(DRAGON_SCALE_ID);
    }

    private SmithingHelper() {
    }

    public static List<String> getInfusionMaterialIds() {
        return Collections.unmodifiableList(INFUSION_MATERIAL_IDS);
    }

    public static List<String> getReinforcementMaterialIds() {
        return Collections.unmodifiableList(REINFORCEMENT_MATERIAL_IDS);
    }

    public static List<String> getSmithingMaterialIds() {
        ArrayList<String> ids = new ArrayList<>(INFUSION_MATERIAL_IDS);
        for (String id : REINFORCEMENT_MATERIAL_IDS) {
            if (!ids.contains(id)) {
                ids.add(id);
            }
        }
        return Collections.unmodifiableList(ids);
    }

    public static boolean isInfusionMaterial(AbstractCard card) {
        return card != null && INFUSION_MATERIAL_IDS.contains(card.cardID);
    }

    public static boolean isReinforcementMaterial(AbstractCard card) {
        return card != null && REINFORCEMENT_MATERIAL_IDS.contains(card.cardID);
    }

    public static boolean isSmithingMaterial(AbstractCard card) {
        return card != null && getSmithingMaterialIds().contains(card.cardID);
    }

    public static int materialCount(AbstractCard infusionMaterial, AbstractCard reinforcementMaterial) {
        int count = 0;
        if (infusionMaterial != null) {
            count++;
        }
        if (reinforcementMaterial != null) {
            count++;
        }
        return count;
    }

    public static int defaultCraftmanCost(AbstractCard body, AbstractCard infusionMaterial,
                                          AbstractCard reinforcementMaterial) {
        if (body == null) {
            return 0;
        }
        if (MURKY_HAND_SCYTHE_ID.equals(body.cardID)) {
            return 0;
        }
        if (setsCraftmanCostToZero(reinforcementMaterial)) {
            return 0;
        }
        int bodyCost = LargeEmber.effectiveBaseCost(body);
        if (bodyCost < 0) {
            return bodyCost;
        }
        int cost = bodyCost + materialCount(infusionMaterial, reinforcementMaterial)
                - craftmanCostReduction(reinforcementMaterial)
                - MoonOfNokstellaPower.currentReduction();
        return Math.max(0, cost);
    }

    public static boolean upgradesInfusionMaterial(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null
                && (TITANITE_SHARD_ID.equals(reinforcementMaterial.cardID)
                || TITANITE_CHUNK_ID.equals(reinforcementMaterial.cardID)
                || TITANITE_SLAB_ID.equals(reinforcementMaterial.cardID));
    }

    public static boolean upgradesBodyCard(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null
                && (TITANITE_CHUNK_ID.equals(reinforcementMaterial.cardID)
                || TITANITE_SLAB_ID.equals(reinforcementMaterial.cardID));
    }

    public static boolean isPassiveReinforcementMaterial(AbstractCard reinforcementMaterial) {
        return upgradesInfusionMaterial(reinforcementMaterial) || upgradesBodyCard(reinforcementMaterial)
                || setsCraftmanCostToZero(reinforcementMaterial)
                || randomizesInfusionMaterialAfterUse(reinforcementMaterial)
                || copiesInfusionMaterial(reinforcementMaterial);
    }

    public static boolean setsCraftmanCostToZero(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null && TWINKLING_TITANITE_ID.equals(reinforcementMaterial.cardID);
    }

    public static boolean randomizesInfusionMaterialAfterUse(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null && DEMON_TITANITE_ID.equals(reinforcementMaterial.cardID);
    }

    public static boolean copiesInfusionMaterial(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null && DRAGON_SCALE_ID.equals(reinforcementMaterial.cardID);
    }

    public static AbstractCard randomInfusionMaterial() {
        if (INFUSION_MATERIAL_IDS.isEmpty()) {
            return null;
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(INFUSION_MATERIAL_IDS.size() - 1);
        return copyForSmithing(CardLibrary.getCopy(INFUSION_MATERIAL_IDS.get(index)));
    }

    public static AbstractCard randomReinforcementMaterial() {
        if (REINFORCEMENT_MATERIAL_IDS.isEmpty()) {
            return null;
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(REINFORCEMENT_MATERIAL_IDS.size() - 1);
        return copyForSmithing(CardLibrary.getCopy(REINFORCEMENT_MATERIAL_IDS.get(index)));
    }

    public static AbstractCard randomSmithingMaterial() {
        List<String> ids = getSmithingMaterialIds();
        if (ids.isEmpty()) {
            return null;
        }
        int index = AbstractDungeon.cardRandomRng == null
                ? 0
                : AbstractDungeon.cardRandomRng.random(ids.size() - 1);
        return copyForSmithing(CardLibrary.getCopy(ids.get(index)));
    }

    private static int craftmanCostReduction(AbstractCard reinforcementMaterial) {
        return reinforcementMaterial != null && TITANITE_SLAB_ID.equals(reinforcementMaterial.cardID) ? 2 : 0;
    }

    public static AbstractCard effectiveInfusionMaterial(AbstractCard infusionMaterial,
                                                         AbstractCard reinforcementMaterial) {
        if (infusionMaterial == null) {
            return null;
        }
        AbstractCard effective = copyForSmithing(infusionMaterial);
        if (upgradesInfusionMaterial(reinforcementMaterial) && effective.canUpgrade()) {
            effective.upgrade();
        }
        return effective;
    }

    public static AbstractCard effectiveBodyCard(AbstractCard body, AbstractCard reinforcementMaterial,
                                                 boolean craftmanCreationUpgraded) {
        if (body == null) {
            return null;
        }
        AbstractCard effective = copyForSmithing(body);
        int upgradeCount = 0;
        if (craftmanCreationUpgraded) {
            upgradeCount++;
        }
        if (upgradesBodyCard(reinforcementMaterial)) {
            upgradeCount++;
        }
        for (int i = 0; i < upgradeCount && effective.canUpgrade(); i++) {
            effective.upgrade();
        }
        return effective;
    }

    public static AbstractCard effectiveReinforcementMaterial(AbstractCard infusionMaterial,
                                                              AbstractCard reinforcementMaterial) {
        if (reinforcementMaterial == null) {
            return null;
        }
        if (copiesInfusionMaterial(reinforcementMaterial)) {
            return effectiveInfusionMaterial(infusionMaterial, null);
        }
        if (isPassiveReinforcementMaterial(reinforcementMaterial)) {
            return null;
        }
        return copyForSmithing(reinforcementMaterial);
    }

    public static AbstractCard.CardTarget combinedTarget(AbstractCard infusionMaterial, AbstractCard body,
                                                         AbstractCard reinforcementMaterial) {
        boolean self = false;
        boolean enemy = false;
        boolean allEnemy = false;
        for (AbstractCard card : new AbstractCard[] { infusionMaterial, body, reinforcementMaterial }) {
            if (card == null) {
                continue;
            }
            switch (card.target) {
                case SELF:
                    self = true;
                    break;
                case ENEMY:
                    enemy = true;
                    break;
                case ALL_ENEMY:
                    allEnemy = true;
                    break;
                case SELF_AND_ENEMY:
                    self = true;
                    enemy = true;
                    break;
                case ALL:
                    self = true;
                    allEnemy = true;
                    break;
                default:
                    break;
            }
        }
        // A single-target component still needs a selected enemy beside any area effects.
        if (enemy) {
            return self ? AbstractCard.CardTarget.SELF_AND_ENEMY : AbstractCard.CardTarget.ENEMY;
        }
        if (self && allEnemy) {
            return AbstractCard.CardTarget.ALL;
        }
        if (allEnemy) {
            return AbstractCard.CardTarget.ALL_ENEMY;
        }
        if (self) {
            return AbstractCard.CardTarget.SELF;
        }
        return AbstractCard.CardTarget.NONE;
    }

    public static int replayAmount(AbstractCard infusionMaterial, AbstractCard body,
                                   AbstractCard reinforcementMaterial) {
        int replay = 0;
        for (AbstractCard card : new AbstractCard[] { infusionMaterial, body, reinforcementMaterial }) {
            if (card != null) {
                replay += ReplayField.getReplay(card);
            }
        }
        return replay;
    }

    public static boolean shouldExhaust(AbstractCard infusionMaterial, AbstractCard body,
                                        AbstractCard reinforcementMaterial) {
        for (AbstractCard card : new AbstractCard[] { infusionMaterial, body, reinforcementMaterial }) {
            if (card != null && card.exhaust) {
                return true;
            }
        }
        return false;
    }

    public static void playComponent(AbstractCard component, AbstractPlayer player, AbstractMonster monster) {
        playComponent(component, player, monster, null);
    }

    public static void playComponent(AbstractCard component, AbstractPlayer player, AbstractMonster monster,
                                     AbstractCard sourceCard) {
        if (component == null) {
            return;
        }
        AbstractCard playable = component.makeStatEquivalentCopy();
        playable.purgeOnUse = true;
        playable.freeToPlayOnce = true;
        playable.energyOnUse = component.energyOnUse;
        if (playable.target == AbstractCard.CardTarget.ENEMY
                || playable.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            if (monster == null || monster.isDeadOrEscaped() || monster.isDying || monster.halfDead) {
                return;
            }
            playable.calculateCardDamage(monster);
        } else {
            playable.applyPowers();
        }
        AbstractCard previousSourceCard = activeCraftmanCreation;
        activeCraftmanCreation = sourceCard;
        try {
            playable.use(player, monster);
        } finally {
            activeCraftmanCreation = previousSourceCard;
        }
    }

    public static UUID getHandAxeReturnUuid(AbstractCard fallbackCard) {
        AbstractCard source = getSmithingSourceCard(fallbackCard);
        return source == null ? null : source.uuid;
    }

    public static AbstractCard getSmithingSourceCard(AbstractCard fallbackCard) {
        return activeCraftmanCreation == null ? fallbackCard : activeCraftmanCreation;
    }

    public static AbstractCard copyForSmithing(AbstractCard card) {
        if (card == null) {
            return null;
        }
        AbstractCard copy = card.makeStatEquivalentCopy();
        copy.costForTurn = copy.cost;
        copy.isCostModifiedForTurn = false;
        copy.freeToPlayOnce = false;
        copy.exhaustOnUseOnce = false;
        return copy;
    }
}
