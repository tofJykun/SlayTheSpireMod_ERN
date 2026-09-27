package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.MadnessPower;

public class NomadicFrenzyflameAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int madness;

    public NomadicFrenzyflameAction(AbstractPlayer player, int madness) {
        this.player = player;
        this.madness = madness;
        this.actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }
        this.isDone = true;
        AbstractMonster monster = AbstractDungeon.getMonsters()
                .getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (monster != null) {
            addToTop(new ApplyPowerAction(monster, this.player,
                    new MadnessPower(monster, this.madness), this.madness));
        }
    }
}
