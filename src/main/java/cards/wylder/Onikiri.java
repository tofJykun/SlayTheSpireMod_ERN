package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Onikiri extends CustomCard {
    public static final String ID = "Onikiri";
    private static final String IMG_PATH = "img/cards/wylder/Onikiri.png";
    private static final int COST = 3;
    private static final int EVEN_LIMIT = 40;
    private static final int UPGRADE_PLUS_EVEN_LIMIT = 20;

    public Onikiri() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = EVEN_LIMIT;
        this.magicNumber = this.baseMagicNumber;
        this.baseDamage = 0;
        this.baseBlock = 0;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int value = calculateValue(p, this.magicNumber);
        if (value > 0) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, value, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, value));
        }
    }

    private static int calculateValue(AbstractPlayer player, int limit) {
        int difference = WylderIntentHelper.incomingDamageBlockGap(player);
        if (difference > 0 && difference <= limit && difference % 2 == 0) {
            return WylderIntentHelper.nextPrimeAtLeast((difference + 1) / 2);
        }
        return 0;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null
                && calculateValue(AbstractDungeon.player, this.magicNumber) > 0
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Onikiri();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EVEN_LIMIT);
        }
    }
}
