package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import patches.AbstractCardEnum;

public class RustedAnchor extends CustomCard {
    public static final String ID = "RustedAnchor";
    private static final String IMG_PATH = "img/cards/undertaker/RustedAnchor.png";
    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public RustedAnchor() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = DRAW;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new ApplyPowerAction(p, p,
                new DrawCardNextTurnPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RustedAnchor();
    }
}
