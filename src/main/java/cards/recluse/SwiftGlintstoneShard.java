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

public class SwiftGlintstoneShard extends CustomCard {
    public static final String ID = "SwiftGlintstoneShard";
    private static final String IMG_PATH = "img/cards/recluse/GlitstonePebble.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int DRAW = 1;

    public SwiftGlintstoneShard() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, DRAW));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        refreshCostByLastPlayedCard();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard card) {
        refreshCost(card.type != CardType.ATTACK);
    }

    @Override
    public void atTurnStart() {
        refreshCostByLastPlayedCard();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshCostByLastPlayedCard();
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = shouldBeFreeByLastPlayedCard()
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private void refreshCostByLastPlayedCard() {
        refreshCost(shouldBeFreeByLastPlayedCard());
    }

    private boolean shouldBeFreeByLastPlayedCard() {
        if (AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            return false;
        }

        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(
                AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);
        return lastCard.type != CardType.ATTACK;
    }

    private void refreshCost(boolean free) {
        if (free) {
            setCostForTurn(0);
        } else {
            this.costForTurn = this.cost;
            this.isCostModifiedForTurn = false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SwiftGlintstoneShard();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
