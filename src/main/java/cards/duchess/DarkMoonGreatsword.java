package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ReplayField;
import powers.PoiseBreakPower;

public class DarkMoonGreatsword extends CustomCard {
    public static final String ID = "DarkMoonGreatsword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public DarkMoonGreatsword() {
        super(ID, STRINGS.NAME, "img/cards/duchess/DarkMoonGreatsword.png", 0, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = 3;
        this.baseMagicNumber = this.magicNumber = 1;
        ReplayField.setReplay(this, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p, new PoiseBreakPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            ReplayField.setReplay(this, 2);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new DarkMoonGreatsword(); }
}
