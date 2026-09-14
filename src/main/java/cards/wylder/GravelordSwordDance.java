package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class GravelordSwordDance extends CustomCard {
    public static final String ID = "GravelordSwordDance";
    private static final String IMG_PATH = "img/cards/wylder/GravelordSwordDance.png";
    private static final int COST = 2;
    private static final int DAMAGE = 24;
    private static final int UPGRADE_PLUS_DMG = 6;

    public GravelordSwordDance() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.cardsToPreview = new Dazed();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                this.multiDamage, DamageInfo.DamageType.NORMAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(new Dazed(),
                Math.max(0, 10 - p.hand.size())));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GravelordSwordDance();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
