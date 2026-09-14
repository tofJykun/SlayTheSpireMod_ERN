package general;

import cards.scholar.AeonianButterfly;
import cards.scholar.AlbinauricBloodclot;
import cards.scholar.ArteriaLeaf;
import cards.scholar.Bloodrose;
import cards.scholar.EyeOfYelough;
import cards.scholar.GoldenCentipede;
import cards.scholar.GravelStone;
import cards.scholar.MiquellaLily;
import cards.scholar.NascentButterfly;
import cards.scholar.RowaFruit;
import cards.scholar.SacramentalBud;
import cards.scholar.TrinaLily;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import powers.AeonianButterflyPower;
import powers.AlbinauricBloodclotPower;
import powers.ArteriaLeafPower;
import powers.BloodrosePower;
import powers.EyeOfYeloughPower;
import powers.GoldenCentipedePower;
import powers.GravelStonePower;
import powers.MiquellaLilyPower;
import powers.NascentButterflyPower;
import powers.RowaFruitPower;
import powers.SacramentalBudPower;
import powers.TrinaLilyPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CrudeDrug {
    private static final ArrayList<String> CRUDE_DRUG_CARD_IDS = new ArrayList<>();
    private static final ArrayList<String> CRUDE_DRUG_POWER_IDS = new ArrayList<>();

    static {
        CRUDE_DRUG_CARD_IDS.add(AeonianButterfly.ID);
        CRUDE_DRUG_CARD_IDS.add(Bloodrose.ID);
        CRUDE_DRUG_CARD_IDS.add(EyeOfYelough.ID);
        CRUDE_DRUG_CARD_IDS.add(GravelStone.ID);
        CRUDE_DRUG_CARD_IDS.add(AlbinauricBloodclot.ID);
        CRUDE_DRUG_CARD_IDS.add(GoldenCentipede.ID);
        CRUDE_DRUG_CARD_IDS.add(TrinaLily.ID);
        CRUDE_DRUG_CARD_IDS.add(MiquellaLily.ID);
        CRUDE_DRUG_CARD_IDS.add(NascentButterfly.ID);
        CRUDE_DRUG_CARD_IDS.add(SacramentalBud.ID);
        CRUDE_DRUG_CARD_IDS.add(ArteriaLeaf.ID);
        CRUDE_DRUG_CARD_IDS.add(RowaFruit.ID);

        CRUDE_DRUG_POWER_IDS.add(AeonianButterflyPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(BloodrosePower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(EyeOfYeloughPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(GravelStonePower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(AlbinauricBloodclotPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(GoldenCentipedePower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(TrinaLilyPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(MiquellaLilyPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(NascentButterflyPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(SacramentalBudPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(ArteriaLeafPower.POWER_ID);
        CRUDE_DRUG_POWER_IDS.add(RowaFruitPower.POWER_ID);
    }

    private CrudeDrug() {
    }

    public static List<String> getCrudeDrugCardIds() {
        return Collections.unmodifiableList(CRUDE_DRUG_CARD_IDS);
    }

    public static List<String> getCrudeDrugPowerIds() {
        return Collections.unmodifiableList(CRUDE_DRUG_POWER_IDS);
    }

    public static boolean isCrudeDrugCard(AbstractCard card) {
        return card != null && CRUDE_DRUG_CARD_IDS.contains(card.cardID);
    }

    public static boolean isCrudeDrugPower(AbstractPower power) {
        return power instanceof AbstractCrudeDrugPower || (power != null && CRUDE_DRUG_POWER_IDS.contains(power.ID));
    }

    public static String getPotDescription(String powerId) {
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(powerId);
        if (powerStrings == null || powerStrings.DESCRIPTIONS == null || powerStrings.DESCRIPTIONS.length == 0) {
            return "";
        }
        return powerStrings.DESCRIPTIONS[0];
    }

    public static void applyDrugEffect(String powerId, AbstractPlayer player, AbstractMonster monster, AbstractCard source) {
        if (monster == null || monster.isDeadOrEscaped()) {
            return;
        }
        if (AeonianButterflyPower.POWER_ID.equals(powerId)) {
            AeonianButterflyPower.applyDrugEffect(player, monster);
        } else if (BloodrosePower.POWER_ID.equals(powerId)) {
            BloodrosePower.applyDrugEffect(player, monster);
        } else if (EyeOfYeloughPower.POWER_ID.equals(powerId)) {
            EyeOfYeloughPower.applyDrugEffect(player, monster);
        } else if (GravelStonePower.POWER_ID.equals(powerId)) {
            GravelStonePower.applyDrugEffect(player, monster);
        } else if (AlbinauricBloodclotPower.POWER_ID.equals(powerId)) {
            AlbinauricBloodclotPower.applyDrugEffect(player, monster);
        } else if (GoldenCentipedePower.POWER_ID.equals(powerId)) {
            GoldenCentipedePower.applyDrugEffect(player, monster);
        } else if (TrinaLilyPower.POWER_ID.equals(powerId)) {
            TrinaLilyPower.applyDrugEffect(player, monster);
        } else if (MiquellaLilyPower.POWER_ID.equals(powerId)) {
            MiquellaLilyPower.applyDrugEffect(player, monster);
        } else if (NascentButterflyPower.POWER_ID.equals(powerId)) {
            NascentButterflyPower.applyDrugEffect(player, monster);
        } else if (SacramentalBudPower.POWER_ID.equals(powerId)) {
            SacramentalBudPower.applyDrugEffect(player, monster);
        } else if (ArteriaLeafPower.POWER_ID.equals(powerId)) {
            ArteriaLeafPower.applyDrugEffect(player, monster);
        } else if (RowaFruitPower.POWER_ID.equals(powerId)) {
            RowaFruitPower.applyDrugEffect(player, monster);
        }
    }

    public static String buildCrudeDrugKeywordDescription(String prefix, String separator, String suffix) {
        StringBuilder builder = new StringBuilder(prefix);
        for (int i = 0; i < CRUDE_DRUG_CARD_IDS.size(); i++) {
            if (i > 0) {
                builder.append(separator);
            }
            CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CRUDE_DRUG_CARD_IDS.get(i));
            builder.append("#y").append(cardStrings.NAME.replace(" ", " #y"));
        }
        builder.append(suffix);
        return builder.toString();
    }

    public static ArrayList<String> firstThreeCrudeDrugPowerIds(AbstractPlayer player) {
        ArrayList<String> ids = new ArrayList<>();
        if (player == null) {
            return ids;
        }
        for (AbstractPower power : player.powers) {
            if (isCrudeDrugPower(power)) {
                int copies = Math.max(1, power.amount);
                for (int i = 0; i < copies; i++) {
                    ids.add(power.ID);
                    if (ids.size() == 3) {
                        return ids;
                    }
                }
            }
        }
        return ids;
    }

    public static void removeFirstThreeCrudeDrugPowers(AbstractPlayer player) {
        if (player == null) {
            return;
        }
        ArrayList<AbstractPower> toRemove = new ArrayList<>();
        ArrayList<Integer> removeAmounts = new ArrayList<>();
        int remaining = 3;
        for (AbstractPower power : player.powers) {
            if (isCrudeDrugPower(power)) {
                int removeAmount = Math.min(Math.max(1, power.amount), remaining);
                toRemove.add(power);
                removeAmounts.add(removeAmount);
                remaining -= removeAmount;
                if (remaining == 0) {
                    break;
                }
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            AbstractPower power = toRemove.get(i);
            int removeAmount = removeAmounts.get(i);
            if (power.amount > removeAmount) {
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.ReducePowerAction(player, player, power, removeAmount));
            } else {
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(player, player, power));
            }
        }
    }
}
