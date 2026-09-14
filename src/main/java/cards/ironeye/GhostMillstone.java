package cards.ironeye;

import basemod.abstracts.CustomCard;
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
        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        setDamageFromVirtualCurrency();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
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
        this.baseDamage = 0;
    }

    private void setDamageFromVirtualCurrency() {
        int tokens = 0;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(VirtualCurrencyPower.POWER_ID)) {
            tokens = AbstractDungeon.player.getPower(VirtualCurrencyPower.POWER_ID).amount;
        }
        this.baseDamage = Math.max(0, tokens) / this.magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhostMillstone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADE_TOKEN_PER_DAMAGE;
            this.magicNumber = this.baseMagicNumber;
        }
    }
}
