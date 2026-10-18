package general;

import basemod.abstracts.CustomSavable;
import cards.scholar.DragonKingGreataxe;
import cards.scholar.LordOfLords;
import cards.scholar.Tyranny;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import powers.BloodburnPower;
import powers.ScarletRotPower;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class LordOfLordsProgress implements CustomSavable<Boolean> {
    public static final String SAVE_KEY = "ERN:LordOfLordsEarned";
    private static boolean earned;
    private static int stage;
    private static final Set<AbstractMonster> dead = Collections.newSetFromMap(new IdentityHashMap<>());

    public static void resetRun() { earned = false; resetCombat(); }
    public static void resetCombat() { stage = 0; dead.clear(); }

    public static void onAilment(AbstractCreature target, AbstractPower power, int amount) {
        if (earned || stage != 0 || !CombatState.isInCombat() || !(target instanceof AbstractMonster)
                || power == null || amount <= 0) return;
        if (PoisonPower.POWER_ID.equals(power.ID) || ScarletRotPower.POWER_ID.equals(power.ID)
                || BloodburnPower.POWER_ID.equals(power.ID)) stage = 1;
    }

    public static void onCardPlayed(AbstractCard card) {
        if (earned || card == null || !CombatState.isInCombat()) return;
        String id = SmithingBody.behavior(card).cardID;
        if (stage == 1 && Tyranny.ID.equals(id)) stage = 2;
        else if (stage == 2 && DragonKingGreataxe.ID.equals(id)) stage = 3;
    }

    public static void onDeath(AbstractMonster monster) {
        if (monster == null || monster.currentHealth > 0 || !monster.isDying || monster.halfDead
                || !dead.add(monster) || earned || stage != 3 || !CombatState.isInCombat()
                || AbstractDungeon.player == null || AbstractDungeon.player.isDead
                || AbstractDungeon.player.isDying) return;
        earned = true;
        stage = 4;
        AbstractCard reward = new LordOfLords();
        UnlockTracker.markCardAsSeen(reward.cardID);
        CardHelper.obtain(reward.cardID, reward.rarity, reward.color);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onObtainCard(reward);
        AbstractDungeon.player.masterDeck.addToTop(reward);
        for (AbstractRelic relic : new ArrayList<>(AbstractDungeon.player.relics)) relic.onMasterDeckChange();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(reward.makeStatEquivalentCopy()));
    }

    @Override
    public Boolean onSave() { return earned; }

    @Override
    public void onLoad(Boolean saved) { earned = Boolean.TRUE.equals(saved); resetCombat(); }
}
