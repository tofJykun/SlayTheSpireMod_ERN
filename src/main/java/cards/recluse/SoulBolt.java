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

public class SoulBolt extends CustomCard {
    public static final String ID = "SoulBolt";
    private static final String IMG_PATH = "img/cards/recluse/SoulBolt.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int DRAW = 2;

    public SoulBolt() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
            configureCostsOnNewCard();
        }
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private void configureCostsOnNewCard() {
        if (AbstractDungeon.actionManager == null) {
            return;
        }

        int newCost = COST;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (card.type == CardType.POWER) {
                newCost--;
            }
            if (newCost < 0) {
                newCost = 0;
            }
            if (ID.equals(general.SmithingBody.behavior(card).cardID)) {
                newCost++;
            }
        }
        setCostForCombat(newCost);
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        if (card.type == CardType.POWER) {
            changeCostForCombat(-1);
        }
        if (ID.equals(general.SmithingBody.behavior(card).cardID)) {
            changeCostForCombat(1);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        configureCostsOnNewCard();
    }

    private void changeCostForCombat(int amount) {
        if (amount == 0) {
            return;
        }

        int newCost = this.cost + amount;
        if (newCost < 0) {
            newCost = 0;
        }
        setCostForCombat(newCost);
    }

    private void setCostForCombat(int newCost) {
        this.cost = newCost;
        this.costForTurn = newCost;
        this.isCostModified = this.cost != COST;
        this.isCostModifiedForTurn = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, DRAW));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SoulBolt();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
