package powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MajorArcanaSunPower extends AbstractPower {
    public static final String POWER_ID = "MajorArcanaSunPower";

    public MajorArcanaSunPower(AbstractCreature owner, int amount) {
        ID = POWER_ID;
        name = CardCrawlGame.languagePack.getPowerStrings(ID).NAME;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0 || card.purgeOnUse) return;
        flash();
        amount--;
        updateDescription();
        if (amount == 0) addToBot(new AbstractGameAction() {
            @Override public void update() {
                if (MajorArcanaSunPower.this.amount <= 0 && owner.getPower(POWER_ID) == MajorArcanaSunPower.this)
                    addToTop(new RemoveSpecificPowerAction(owner, owner, MajorArcanaSunPower.this));
                isDone = true;
            }
        });
        AbstractMonster target = action.target instanceof AbstractMonster ? (AbstractMonster)action.target : null;
        AbstractCard copy = card.makeSameInstanceOf();
        copy.purgeOnUse = true;
        AbstractDungeon.player.limbo.addToBottom(copy);
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, target, card.energyOnUse, true, true), true);
    }

    @Override public void updateDescription() {
        String[] text = CardCrawlGame.languagePack.getPowerStrings(ID).DESCRIPTIONS;
        description = text[0] + amount + text[1];
    }
}
