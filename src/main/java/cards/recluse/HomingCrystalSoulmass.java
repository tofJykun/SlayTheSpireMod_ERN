package cards.recluse;

import actions.RandomDamageWithPowersAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class HomingCrystalSoulmass extends CustomCard {
    public static final String ID = "HomingCrystalSoulmass";
    private static final String IMG_PATH = "img/cards/recluse/CrystalSoulSpear.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int HIT_COUNT = 4;

    public HomingCrystalSoulmass() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new RandomDamageWithPowersAction((AbstractCreature)p, this,
                    this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HomingCrystalSoulmass();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
