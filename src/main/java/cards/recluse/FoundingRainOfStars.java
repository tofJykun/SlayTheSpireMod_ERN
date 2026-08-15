package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import patches.AbstractCardEnum;

public class FoundingRainOfStars extends CustomCard {
    public static final String ID = "FoundingRainOfStars";
    private static final String IMG_PATH = "img/cards/recluse/FoundingRainOfStars.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 3;
    private static final int HIT_COUNT = 4;
    private static final int UPGRADE_PLUS_HIT_COUNT = 1;

    public FoundingRainOfStars() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = HIT_COUNT;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction(
                    (AbstractGameEffect)new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAllEnemiesAction(
                    (AbstractCreature)p, this.multiDamage, this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        resetHitCount();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        resetHitCount();
    }

    private void resetHitCount() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new FoundingRainOfStars();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HIT_COUNT);
        }
    }
}
