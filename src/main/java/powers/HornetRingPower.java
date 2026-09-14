package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HornetRingPower extends AbstractPower {
    public static final String POWER_ID = "HornetRingPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public HornetRingPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
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

    public static void modifyCardDamage(AbstractCard card, AbstractMonster target) {
        if (card == null || target == null || card.type != AbstractCard.CardType.ATTACK
                || card.damageTypeForTurn != DamageInfo.DamageType.NORMAL || card.damage <= 0
                || AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(POWER_ID)) {
            return;
        }
        card.damage = modifyDamageAmount(card.damage, AbstractDungeon.player, target);
        card.isDamageModified = card.damage != card.baseDamage;
    }

    public static void modifyCardMultiDamage(AbstractCard card) {
        if (card == null || card.type != AbstractCard.CardType.ATTACK
                || card.damageTypeForTurn != DamageInfo.DamageType.NORMAL || card.multiDamage == null
                || AbstractDungeon.player == null || !AbstractDungeon.player.hasPower(POWER_ID)
                || AbstractDungeon.getMonsters() == null) {
            return;
        }
        for (int i = 0; i < card.multiDamage.length && i < AbstractDungeon.getMonsters().monsters.size(); i++) {
            AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
            if (monster == null || monster.isDeadOrEscaped() || card.multiDamage[i] <= 0) {
                continue;
            }
            int oldDamage = card.multiDamage[i];
            card.multiDamage[i] = modifyDamageAmount(card.multiDamage[i], AbstractDungeon.player, monster);
            if (card.multiDamage[i] != oldDamage || card.multiDamage[i] != card.baseDamage) {
                card.isDamageModified = true;
            }
        }
        if (card.multiDamage.length > 0) {
            card.damage = card.multiDamage[0];
        }
    }

    public static void modifyDamage(DamageInfo info, AbstractCreature owner, AbstractCreature target) {
        if (info == null || owner == null || target == null || !owner.isPlayer
                || !(target instanceof AbstractMonster) || info.type != DamageInfo.DamageType.NORMAL
                || info.output <= 0 || !owner.hasPower(POWER_ID)) {
            return;
        }
        int oldOutput = info.output;
        info.output = modifyDamageAmount(info.output, owner, (AbstractMonster)target);
        if (info.output != oldOutput || info.output != info.base) {
            info.isModified = true;
        }
    }

    private static int modifyDamageAmount(int damage, AbstractCreature owner, AbstractMonster target) {
        if (!isAttackIntent(target.intent)) {
            return damage;
        }
        AbstractPower power = owner.getPower(POWER_ID);
        if (power == null || power.amount <= 0) {
            return damage;
        }
        return (int)Math.floor(damage * (100.0F + power.amount) / 100.0F);
    }

    public static boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK
                || intent == AbstractMonster.Intent.ATTACK_BUFF
                || intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
