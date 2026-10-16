package cards.guardian;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import general.CombatState;

public class Bonewheel extends CustomCard {
    public static final String ID = "Bonewheel";
    private static final String IMG_PATH = "img/cards/guardian/Bonewheel.png";
    private static final int COST = -1;
    private static final int HIT_COUNT = 9;
    private static final int UPGRADE_PLUS_HIT_COUNT = 3;
    private boolean combatPreviewCopy;

    public Bonewheel() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = HIT_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Only execution uses the queued X; previews must ignore default/stale energyOnUse.
        this.baseDamage = getXValue(p, this.energyOnUse == -1 ? EnergyPanel.totalCount : this.energyOnUse);
        super.calculateCardDamage(m);
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void applyPowers() {
        boolean combat = usesCombatPreview();
        this.baseDamage = combat ? getXValue(AbstractDungeon.player, EnergyPanel.totalCount) : 0;
        if (combat) super.applyPowers();
        else { this.damage = 0; this.isDamageModified = false; }
        refreshDescription(combat);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (!usesCombatPreview()) {
            applyPowers();
            return;
        }
        this.baseDamage = getXValue(AbstractDungeon.player, EnergyPanel.totalCount);
        super.calculateCardDamage(mo);
        refreshDescription(true);
    }

    private boolean usesCombatPreview() {
        if (!CombatState.isInCombat()) return false;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.masterDeck.contains(this)) return false;
        return combatPreviewCopy || p.hand.contains(this) || p.drawPile.contains(this)
                || p.discardPile.contains(this) || p.exhaustPile.contains(this)
                || p.limbo.contains(this) || p.cardInUse == this;
    }

    private void refreshDescription(boolean combat) {
        String description = combat ? getCardStrings().EXTENDED_DESCRIPTION[0] : getCardStrings().DESCRIPTION;
        if (!description.equals(rawDescription)) {
            rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public void render(SpriteBatch sb, boolean selected) {
        boolean combat = usesCombatPreview();
        if (!combat || !AbstractDungeon.player.hand.contains(this)
                || baseDamage != getXValue(AbstractDungeon.player, EnergyPanel.totalCount)) applyPowers();
        refreshDescription(combat);
        super.render(sb, selected);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Bonewheel copy = (Bonewheel)super.makeStatEquivalentCopy();
        // The single-card popup uses a copy outside every combat pile.
        copy.combatPreviewCopy = usesCombatPreview();
        copy.applyPowers();
        return copy;
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = 0;
    }

    private int getXValue(AbstractPlayer p, int value) {
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bonewheel();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HIT_COUNT);
        }
    }
}
