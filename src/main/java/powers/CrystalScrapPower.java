package powers;

import actions.RandomDamageWithPowersAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CrystalScrapPower extends AbstractPower {
    public static final String POWER_ID = "CrystalScrapPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static int powerIdOffset;

    private final AbstractCard card;
    private final int damage;
    private final int hitCount;
    private final DamageInfo.DamageType damageType;
    private boolean waitingForNextTurn = true;

    public CrystalScrapPower(AbstractCreature owner, int turns, AbstractCard card, int damage,
                             int hitCount, DamageInfo.DamageType damageType) {
        this.name = NAME;
        this.ID = POWER_ID + powerIdOffset;
        powerIdOffset++;
        this.owner = owner;
        this.amount = turns;
        this.card = card;
        this.damage = damage;
        this.hitCount = hitCount;
        this.damageType = damageType;
        this.type = PowerType.BUFF;
        PowerIconHelper.load(this, POWER_ID);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            if (this.waitingForNextTurn) {
                this.waitingForNextTurn = false;
                updateDescription();
                return;
            }
            addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            if (this.amount == 1) {
                for (int i = 0; i < this.hitCount; i++) {
                    addToBot(new RandomDamageWithPowersAction(this.owner, this.card, this.damage,
                            this.damageType, AbstractGameAction.AttackEffect.BLUNT_LIGHT, false));
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (!this.waitingForNextTurn) {
            this.description = String.format(DESCRIPTIONS[1], Integer.valueOf(this.damage),
                    Integer.valueOf(this.hitCount));
        } else {
            this.description = String.format(DESCRIPTIONS[0], Integer.valueOf(this.damage),
                    Integer.valueOf(this.hitCount));
        }
    }
}
