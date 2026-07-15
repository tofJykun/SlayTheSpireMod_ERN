package cards.guardian;

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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;

public class ChargeForth extends CustomCard {
    public static final String ID = "ChargeForth";
    private static final String IMG_PATH = "img/cards/guardian/ChargeForth.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public ChargeForth() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        refreshCost();
    }

    @Override
    public void atTurnStart() {
        refreshCost();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshCost();
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = shouldBeFree()
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private void refreshCost() {
        if (shouldBeFree()) {
            setCostForTurn(0);
        } else {
            this.costForTurn = this.cost;
            this.isCostModifiedForTurn = false;
        }
    }

    private boolean shouldBeFree() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasPower(VigorPower.POWER_ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChargeForth();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }
}
