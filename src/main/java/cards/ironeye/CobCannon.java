package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class CobCannon extends CustomCard {
    public static final String ID = "CobCannon";
    private static final String IMG_PATH = "img/cards/ironeye/CobCannon.png";
    private static final int COST = 5;
    private static final int ATTACK_DMG = 28;
    private static final int UPGRADE_PLUS_DMG = 8;
    private static final int DRAW = 1;

    public CobCannon() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        refreshCost();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        refreshCost();
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, DRAW));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        refreshAllCobCannons();
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        refreshAllCobCannons();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard card) {
        refreshAllCobCannons();
    }

    @Override
    public void onMoveToDiscard() {
        refreshAllCobCannons();
    }

    @Override
    public void triggerOnExhaust() {
        refreshAllCobCannons();
    }

    @Override
    public void atTurnStart() {
        refreshAllCobCannons();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshCost();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        refreshCost();
    }

    @Override
    public void triggerOnGlowCheck() {
        refreshCost();
    }

    private void refreshCost() {
        int count = countCombatCobCannons();
        int newCost = COST - count;
        if (newCost < 0) {
            newCost = 0;
        }
        this.costForTurn = newCost;
        this.isCostModifiedForTurn = this.costForTurn != this.cost;
    }

    public static void refreshAllCobCannons() {
        if (AbstractDungeon.player == null) {
            return;
        }

        Set<AbstractCard> cards = collectCombatCards();
        for (AbstractCard card : cards) {
            if (card instanceof CobCannon) {
                ((CobCannon)card).refreshCost();
            }
        }
    }

    private static int countCombatCobCannons() {
        int count = 0;
        for (AbstractCard card : collectCombatCards()) {
            if (ID.equals(card.cardID)) {
                count++;
            }
        }
        return count;
    }

    private static Set<AbstractCard> collectCombatCards() {
        Set<AbstractCard> cards = Collections.newSetFromMap(new IdentityHashMap<AbstractCard, Boolean>());
        if (AbstractDungeon.player == null) {
            return cards;
        }

        addGroup(cards, AbstractDungeon.player.drawPile);
        addGroup(cards, AbstractDungeon.player.hand);
        addGroup(cards, AbstractDungeon.player.discardPile);
        addGroup(cards, AbstractDungeon.player.exhaustPile);
        addGroup(cards, AbstractDungeon.player.limbo);

        if (AbstractDungeon.actionManager != null) {
            for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
                if (item != null && item.card != null) {
                    cards.add(item.card);
                }
            }
        }
        return cards;
    }

    private static void addGroup(Set<AbstractCard> cards, CardGroup group) {
        if (group == null) {
            return;
        }
        cards.addAll(group.group);
    }

    @Override
    public AbstractCard makeCopy() {
        return new CobCannon();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
