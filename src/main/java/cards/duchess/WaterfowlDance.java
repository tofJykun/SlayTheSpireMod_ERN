package cards.duchess;

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

public class WaterfowlDance extends CustomCard {
    public static final String ID = "WaterfowlDance";
    private static final String IMG_PATH = "img/cards/duchess/WaterfowlDance.png";
    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    public WaterfowlDance() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        this.magicNumber = count;
        for (int i = 0; i < count; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDynamicDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updateDynamicDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    private void updateDynamicDescription() {
        int previewCount = 1;
        if (AbstractDungeon.actionManager != null) {
            previewCount = AbstractDungeon.actionManager.cardsPlayedThisTurn.size() + 1;
        }
        this.baseMagicNumber = previewCount;
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new WaterfowlDance();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
