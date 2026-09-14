package cards.guardian;

import basemod.abstracts.CustomCard;
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
import patches.AbstractCardEnum;
import powers.HoverPower;

public class FeatherFan extends CustomCard {
    public static final String ID = "FeatherFan";
    private static final String IMG_PATH = "img/cards/guardian/FeatherFan.png";
    private static final int COST = 1;
    private static final int MULTIPLIER = 4;
    private static final int UPGRADE_PLUS_MULTIPLIER = 2;

    public FeatherFan() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = MULTIPLIER;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void applyPowers() {
        setDamageFromHover();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromHover();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = 0;
    }

    private void setDamageFromHover() {
        int hover = 0;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(HoverPower.POWER_ID)) {
            hover = AbstractDungeon.player.getPower(HoverPower.POWER_ID).amount;
        }
        this.baseDamage = Math.max(0, hover) * this.magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FeatherFan();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER);
        }
    }
}
