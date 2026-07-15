package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Ubadachi extends CustomCard {
    public static final String ID = "Ubadachi";
    private static final String IMG_PATH = "img/cards/wylder/Ubadachi.png";
    private static final int COST = 1;
    private static final int PRIME_LIMIT = 20;
    private static final int UPGRADE_PLUS_PRIME_LIMIT = 10;

    public Ubadachi() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = PRIME_LIMIT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int blockToGain = calculatePrimeBlock(p, this.magicNumber);
        if (blockToGain > 0) {
            addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, blockToGain));
        }
    }

    private static int calculatePrimeBlock(AbstractPlayer player, int limit) {
        int difference = WylderIntentHelper.incomingDamageBlockGap(player);
        if (difference > 0 && difference <= limit && WylderIntentHelper.isPrime(difference)) {
            return difference;
        }
        return 0;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null
                && calculatePrimeBlock(AbstractDungeon.player, this.magicNumber) > 0
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ubadachi();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PRIME_LIMIT);
        }
    }
}
