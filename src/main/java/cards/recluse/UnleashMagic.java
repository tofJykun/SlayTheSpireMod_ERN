package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class UnleashMagic extends CustomCard {
    public static final String ID = "UnleashMagic";
    private static final String IMG_PATH = "img/cards/recluse/UnleashMagic.png";
    private static final int COST = 2;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    public UnleashMagic() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int powerCount = countPowerCardsPlayedThisCombat();
        for (int i = 0; i < powerCount; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    private int countPowerCardsPlayedThisCombat() {
        if (AbstractDungeon.actionManager == null) {
            return 0;
        }
        int count = 0;
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (card.type == CardType.POWER) {
                count++;
            }
        }
        return count;
    }

    private void updatePowerCardsPlayedDisplay() {
        this.baseMagicNumber = countPowerCardsPlayedThisCombat();
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        initializeDescription();
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

    @Override
    public void onMoveToDiscard() {
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnleashMagic();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
