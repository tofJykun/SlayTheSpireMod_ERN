package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class ForbiddenSun extends CustomCard {
    public static final String ID = "ForbiddenSun";
    private static final String IMG_PATH = "img/cards/undertaker/ForbiddenSun.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 28;
    private static final int UPGRADE_PLUS_DMG = 8;

    public ForbiddenSun() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.isMultiDamage = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Burn();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p, this.multiDamage,
                this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        addToBot((AbstractGameAction)new ExhaustAction(this.magicNumber, true, false, false));
        addToBot(new MakeTempCardInHandAction(new Burn(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ForbiddenSun();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
