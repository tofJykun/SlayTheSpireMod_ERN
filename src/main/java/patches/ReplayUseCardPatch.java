package patches;

import actions.ReplayCardAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReplayUseCardPatch {
    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCard.class, AbstractCreature.class })
    public static class ConstructorPatch {
        @SpirePostfixPatch
        public static void postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (card == null || AbstractDungeon.actionManager == null) {
                return;
            }

            AbstractMonster monster = target instanceof AbstractMonster ? (AbstractMonster)target : null;
            int remaining = ReplayField.isReplayCopy(card)
                    ? ReplayField.getRemainingReplay(card)
                    : ReplayField.getReplay(card);
            if (remaining > 0) {
                AbstractDungeon.actionManager.addToBottom(new ReplayCardAction(card, monster, remaining));
            }
        }
    }
}
