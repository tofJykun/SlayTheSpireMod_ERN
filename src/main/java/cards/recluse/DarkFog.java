package cards.recluse;

import basemod.abstracts.CustomCard;
import cards.status.MagicEmber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
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

public class DarkFog extends CustomCard {
    public static final String ID = "DarkFog";
    private static final String IMG_PATH = "img/cards/recluse/FoundingRainOfStars.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int HIT_COUNT = 2;
    private static final int MAGIC_EMBERS = 1;

    public DarkFog() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new VFXAction(
                    (AbstractGameEffect)new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F));
            addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                    this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(new MagicEmber(), MAGIC_EMBERS));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new DarkFog();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
