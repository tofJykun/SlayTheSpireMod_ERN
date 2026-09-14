package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;

import java.util.ArrayList;

public class Greatsword extends CustomCard {
    public static final String ID = "Greatsword";
    private static final String IMG_PATH = "img/cards/wylder/Greatsword.png";
    private static final int COST = 2;
    private static final int DAMAGE = 30;
    private static final int UPGRADE_PLUS_DMG = 10;

    public Greatsword() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs.add(power);
            }
        }

        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        for (AbstractPower power : debuffs) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction((AbstractCreature)p,
                    (AbstractCreature)p, power.ID));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Greatsword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
