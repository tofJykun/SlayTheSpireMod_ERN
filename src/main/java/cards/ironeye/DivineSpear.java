package cards.ironeye;

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

public class DivineSpear extends CustomCard {
    public static final String ID = "DivineSpear";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/DivineSpear.png";
    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int EXTRA_DAMAGE_PER_RARE = 5;
    private static final int UPGRADE_PLUS_EXTRA_DAMAGE = 2;

    public DivineSpear() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = EXTRA_DAMAGE_PER_RARE;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void applyPowers() {
        setDamageFromMasterDeck();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromMasterDeck();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = DAMAGE;
    }

    private void setDamageFromMasterDeck() {
        this.baseDamage = DAMAGE + countRareCardsInMasterDeck() * this.magicNumber;
    }

    private int countRareCardsInMasterDeck() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return 0;
        }

        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.rarity == CardRarity.RARE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public AbstractCard makeCopy() {
        return new DivineSpear();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EXTRA_DAMAGE);
        }
    }
}
