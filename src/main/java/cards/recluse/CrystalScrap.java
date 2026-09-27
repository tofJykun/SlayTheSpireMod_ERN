package cards.recluse;

import actions.CrystalScrapAction;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.AbstractCardEnum;

public class CrystalScrap extends CustomCard {
    public static final String ID = "CrystalScrap";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/recluse/CrystalScrap.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int HIT_COUNT = 3;

    public CrystalScrap() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = this.magicNumber = HIT_COUNT;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CrystalScrapAction(p, this, this.baseDamage, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.damage = calculateMagicDamage(null, false);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster target) {
        this.damage = calculateMagicDamage(target, true);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    private int calculateMagicDamage(AbstractMonster target, boolean includeTargetPowers) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            return this.baseDamage;
        }
        float tmp = this.baseDamage;
        for (AbstractRelic relic : player.relics) {
            tmp = relic.atDamageModify(tmp, this);
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageGive(tmp, this.damageTypeForTurn, this);
        }
        tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
        if (includeTargetPowers && target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageReceive(tmp, this.damageTypeForTurn);
            }
        }
        for (AbstractPower power : player.powers) {
            tmp = power.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
        }
        if (includeTargetPowers && target != null && !target.isDying && !target.isEscaping) {
            for (AbstractPower power : target.powers) {
                tmp = power.atDamageFinalReceive(tmp, this.damageTypeForTurn);
            }
        }
        return MathUtils.floor(Math.max(0.0F, tmp));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrystalScrap();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(1);
        }
    }
}
