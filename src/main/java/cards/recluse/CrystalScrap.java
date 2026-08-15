package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.CrystalScrapPower;

public class CrystalScrap extends CustomCard {
    public static final String ID = "CrystalScrap";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/recluse/CrystalScrap.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int HIT_COUNT = 6;
    private static final int DELAY_TURNS = 1;

    public CrystalScrap() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = HIT_COUNT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new CrystalScrapPower((AbstractCreature)p, DELAY_TURNS, makeStatEquivalentCopy(),
                        this.damage, HIT_COUNT, this.damageTypeForTurn), DELAY_TURNS));
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
        return new CrystalScrap();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
