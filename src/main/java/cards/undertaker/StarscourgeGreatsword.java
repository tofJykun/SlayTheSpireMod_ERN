package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import general.CombatState;
import patches.AbstractCardEnum;

import java.util.IdentityHashMap;

public class StarscourgeGreatsword extends CustomCard {
    public static final String ID = "StarscourgeGreatsword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/undertaker/StarscourgeGreatsword.png";
    private static final int COST = -1;
    private static final int REPEAT_THRESHOLD = 3;

    private int repeatedX = -1;
    private int repeatedOutgoingDamage;
    private IdentityHashMap<AbstractMonster, Integer> repeatedDamage;

    public StarscourgeGreatsword() {
        super(ID, STRINGS.NAME, IMG_PATH, COST, STRINGS.DESCRIPTION.replace("!MX!", Integer.toString(REPEAT_THRESHOLD)),
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 6;
        this.isMultiDamage = true;
    }

    private int rawX() {
        return Math.max(0, this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse);
    }

    private int effectiveX() {
        if (this.repeatedX >= 0) {
            return this.repeatedX;
        }
        int x = rawX();
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("Chemical X")) {
            x += 2;
        }
        return x;
    }

    @Override
    public void applyPowers() {
        calculateCardDamage(null);
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        if (this.repeatedDamage != null) {
            restoreRepeatedDamage();
            return;
        }
        int originalBase = this.baseDamage;
        try {
            this.baseDamage = (int)Math.min(Integer.MAX_VALUE,
                    (long)originalBase + (long)this.magicNumber * effectiveX());
            super.calculateCardDamage(monster);
        } finally {
            this.baseDamage = originalBase;
        }
    }

    private int outgoingDamage(int x) {
        int originalBase = this.baseDamage;
        try {
            this.isMultiDamage = false;
            this.baseDamage = (int)Math.min(Integer.MAX_VALUE,
                    (long)originalBase + (long)this.magicNumber * x);
            super.applyPowers();
            return this.damage;
        } finally {
            this.baseDamage = originalBase;
            this.isMultiDamage = true;
        }
    }

    private void restoreRepeatedDamage() {
        this.damage = this.repeatedOutgoingDamage;
        this.multiDamage = new int[AbstractDungeon.getMonsters().monsters.size()];
        for (int i = 0; i < this.multiDamage.length; i++) {
            AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
            Integer saved = this.repeatedDamage.get(monster);
            if (saved != null) {
                this.multiDamage[i] = saved;
            } else {
                // Newly spawned enemies were not in the original AoE; keep the saved player-side damage.
                DamageInfo info = new DamageInfo(AbstractDungeon.player, this.repeatedOutgoingDamage, this.damageTypeForTurn);
                info.applyEnemyPowersOnly(monster);
                this.multiDamage[i] = info.output;
            }
        }
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int rawEnergy = rawX();
        int x = effectiveX();
        StarscourgeGreatsword repeat = null;
        if (this.repeatedDamage == null && !this.purgeOnUse && x >= REPEAT_THRESHOLD) {
            int outgoing = outgoingDamage(x);
            calculateCardDamage(monster);
            repeat = (StarscourgeGreatsword)makeSameInstanceOf();
            repeat.repeatedX = x;
            repeat.repeatedOutgoingDamage = outgoing;
            repeat.repeatedDamage = new IdentityHashMap<>();
            for (int i = 0; i < this.multiDamage.length; i++) {
                repeat.repeatedDamage.put(AbstractDungeon.getMonsters().monsters.get(i), this.multiDamage[i]);
            }
        } else {
            calculateCardDamage(monster);
        }
        addToBot(new DamageAllEnemiesAction(player, this.multiDamage.clone(), this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (repeat != null) {
            final StarscourgeGreatsword copy = repeat;
            copy.purgeOnUse = true;
            copy.freeToPlayOnce = true;
            copy.ignoreEnergyOnUse = true;
            copy.energyOnUse = rawEnergy;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if (!CombatState.isInCombat() || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        return;
                    }
                    player.limbo.addToBottom(copy);
                    copy.current_x = current_x;
                    copy.current_y = current_y;
                    copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    copy.target_y = Settings.HEIGHT / 2.0F;
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, null, rawEnergy, true, true), true);
                }
            });
        }
        if (this.repeatedDamage == null && player.hasRelic("Chemical X")) {
            player.getRelic("Chemical X").flash();
        }
        if (!this.freeToPlayOnce && this.repeatedDamage == null) {
            player.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        StarscourgeGreatsword copy = (StarscourgeGreatsword)super.makeStatEquivalentCopy();
        copy.repeatedX = this.repeatedX;
        copy.repeatedOutgoingDamage = this.repeatedOutgoingDamage;
        if (this.repeatedDamage != null) {
            copy.repeatedDamage = new IdentityHashMap<>(this.repeatedDamage);
        }
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new StarscourgeGreatsword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }
}
