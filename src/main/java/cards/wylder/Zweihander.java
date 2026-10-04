package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;
import powers.StunPower;

public class Zweihander extends CustomCard {
    public static final String ID = "Zweihander";
    private static final int BONUS_POISE_BREAK = 4;

    public Zweihander() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/wylder/Zweihander.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION
                        .replace("!MD!", Integer.toString(BONUS_POISE_BREAK)),
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 20;
        baseMagicNumber = magicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        // Snapshot before this card's own Poise Break can cause a new stun.
        boolean stunned = m.hasPower(StunPower.POWER_ID) || m.intent == AbstractMonster.Intent.STUN;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot(new ApplyPowerAction(m, p, new PoiseBreakPower(m, magicNumber), magicNumber));
        if (stunned) {
            addToBot(new ApplyPowerAction(m, p,
                    new PoiseBreakPower(m, BONUS_POISE_BREAK), BONUS_POISE_BREAK));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(8);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Zweihander();
    }
}
