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

public class WingedKnightHalberd extends CustomCard {
    public static final String ID = "WingedKnightHalberd";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/guardian/WingedKnightHalberd.png";
    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int BLOCK_STEP = 10;
    private static final int UPGRADE_PLUS_BLOCK_STEP = -4;

    public WingedKnightHalberd() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = BLOCK_STEP;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void applyPowers() {
        setDamageFromBlock();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromBlock();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = DAMAGE;
    }

    private void setDamageFromBlock() {
        int block = AbstractDungeon.player == null ? 0 : Math.max(0, AbstractDungeon.player.currentBlock);
        int step = Math.max(1, this.magicNumber);
        this.baseDamage = DAMAGE * (1 + block / step);
    }

    @Override
    public AbstractCard makeCopy() {
        return new WingedKnightHalberd();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK_STEP);
        }
    }
}
