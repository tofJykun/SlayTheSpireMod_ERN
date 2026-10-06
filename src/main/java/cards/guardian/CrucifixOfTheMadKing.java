package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.HoverPower;

public class CrucifixOfTheMadKing extends CustomCard {
    public static final String ID = "CrucifixOfTheMadKing";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrucifixOfTheMadKing() {
        super(ID, STRINGS.NAME, "img/cards/guardian/CrucifixOfTheMadKing.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 12;
        baseMagicNumber = magicNumber = 1;
        isMultiDamage = true;
    }

    public int getSelfDamage() {
        return upgraded ? 14 : 12;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage.clone(), damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        // One draw from Discovery's RNG; no randomness during preview or construction.
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                addToBot(new DamageAllEnemiesAction(p, multiDamage.clone(), damageTypeForTurn,
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 1:
                addToBot(new ApplyPowerAction(p, p, new HoverPower(p, magicNumber), magicNumber));
                break;
            default:
                // Burn-like, blockable self damage, not an attack against the player.
                addToBot(new DamageAction(p, new DamageInfo(p, getSelfDamage(), DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.FIRE));
                break;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrucifixOfTheMadKing();
    }
}
