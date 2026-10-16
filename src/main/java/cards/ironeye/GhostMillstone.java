package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.VirtualCurrencyPower;
import general.CombatState;

public class GhostMillstone extends CustomCard {
    public static final String ID = "GhostMillstone";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/GhostMillstone.png";
    private static final int COST = 1;
    private static final int TOKEN_PER_DAMAGE = 3;
    private static final int UPGRADE_TOKEN_PER_DAMAGE = 2;

    public GhostMillstone() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = TOKEN_PER_DAMAGE;
        this.magicNumber = this.baseMagicNumber;
        this.baseDamage = this.damage = 0;
        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        setDamageFromVirtualCurrency();
        if (hasCombatPlayer()) super.applyPowers();
        else { this.damage = 0; this.isDamageModified = false; }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (!hasCombatPlayer()) { applyPowers(); return; }
        setDamageFromVirtualCurrency();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void onMoveToDiscard() {
        applyPowers();
    }

    private boolean hasCombatPlayer() {
        // Card previews are also constructed before AbstractDungeon can safely initialize.
        return CardCrawlGame.dungeon != null && AbstractDungeon.player != null && CombatState.isInCombat();
    }

    private int tokenDamage() {
        int tokens = 0;
        if (hasCombatPlayer() && AbstractDungeon.player.hasPower(VirtualCurrencyPower.POWER_ID)) {
            tokens = AbstractDungeon.player.getPower(VirtualCurrencyPower.POWER_ID).amount;
        }
        return Math.max(0, tokens) / this.magicNumber;
    }

    private void setDamageFromVirtualCurrency() {
        this.baseDamage = tokenDamage();
    }

    @Override
    public void update() {
        super.update();
        if (this.baseDamage != tokenDamage()) applyPowers();
    }

    @Override
    public void render(SpriteBatch sb, boolean selected) {
        if (!hasCombatPlayer() || !AbstractDungeon.player.hand.contains(this)) applyPowers();
        super.render(sb, selected);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.applyPowers();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhostMillstone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TOKEN_PER_DAMAGE - this.baseMagicNumber);
            applyPowers();
        }
    }
}
