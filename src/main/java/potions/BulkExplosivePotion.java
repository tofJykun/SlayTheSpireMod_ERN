package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class BulkExplosivePotion extends AbstractPotion {
    public static final String POTION_ID = "BulkExplosivePotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BulkExplosivePotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.H, PotionColor.EXPLOSIVE);
        this.isThrown = true;
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
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
            }
        }
        addToBot((AbstractGameAction)new WaitAction(0.5F));
        addToBot((AbstractGameAction)new DamageAllEnemiesAction(null,
                DamageInfo.createDamageMatrix(this.potency, true), DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 10;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BulkExplosivePotion();
    }
}
