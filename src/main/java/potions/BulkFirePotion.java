package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class BulkFirePotion extends AbstractPotion {
    public static final String POTION_ID = "BulkFirePotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BulkFirePotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.FIRE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1]
                + potionStrings.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        DamageInfo info = new DamageInfo((AbstractCreature)AbstractDungeon.player, this.potency,
                DamageInfo.DamageType.THORNS);
        info.applyEnemyPowersOnly(target);
        addToBot((AbstractGameAction)new DamageAction(target, info, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 20;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BulkFirePotion();
    }
}
