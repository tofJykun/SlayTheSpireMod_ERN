package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import general.CombatState;

public class SoulFlash extends CustomCard {
    public static final String ID = "SoulFlash";
    private static final String IMG_PATH = "img/cards/recluse/SoulFlash.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    public SoulFlash() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        int drawCount = countPowerCardsPlayedThisTurn();
        if (drawCount > 0) {
            addToBot(new DrawCardAction(p, drawCount));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updatePowerCardsPlayedDisplay();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updatePowerCardsPlayedDisplay();
    }

    private int countPowerCardsPlayedThisTurn() {
        if (!CombatState.isInCombat() || AbstractDungeon.actionManager == null) {
            return 0;
        }
        int count = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (card.type == CardType.POWER) {
                count++;
            }
        }
        return count;
    }

    private void updatePowerCardsPlayedDisplay() {
        this.baseMagicNumber = countPowerCardsPlayedThisTurn();
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SoulFlash();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
