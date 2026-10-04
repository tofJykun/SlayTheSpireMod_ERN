package cards.guardian;

import actions.BastardSwordAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class BastardSword extends CustomCard {
    public static final String ID = "BastardSword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public BastardSword() {
        this(true);
    }

    private BastardSword(boolean preview) {
        super(ID, STRINGS.NAME, "img/cards/guardian/BastardSword.png", 2, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 15;
        baseMagicNumber = magicNumber = 7;
        exhaust = true;
        if (preview) {
            cardsToPreview = new BastardSword(false);
        }
    }

    private int deckDamageBonus() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return 0;
        }
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (ID.equals(card.cardID)) {
                count++;
            }
        }
        return count * magicNumber;
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = baseDamage;
        baseDamage += deckDamageBonus();
        try {
            super.applyPowers();
        } finally {
            baseDamage = originalBaseDamage;
        }
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = baseDamage;
        baseDamage += deckDamageBonus();
        try {
            super.calculateCardDamage(monster);
        } finally {
            baseDamage = originalBaseDamage;
        }
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            calculateCardDamage(m);
            addToBot(new BastardSwordAction(p, m,
                    new DamageInfo(p, damage, damageTypeForTurn), upgraded));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
            if (cardsToPreview != null) {
                cardsToPreview.upgrade();
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BastardSword();
    }
}
