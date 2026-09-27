package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Purge extends CustomCard {
    public static final String ID = "Purge";
    private static final String IMG_PATH = "img/cards/undertaker/Purge.png";
    private static final int COST = 2;
    private static final int ATTACK_DAMAGE = 15;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public Purge() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DAMAGE;
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, this.magicNumber));
        addToBot((AbstractGameAction)new DiscardAction((AbstractCreature)p, (AbstractCreature)p, 1, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Purge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }
}
