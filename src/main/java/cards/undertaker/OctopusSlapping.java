package cards.undertaker;

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
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;
import powers.PoiseBreakPower;

public class OctopusSlapping extends CustomCard {
    public static final String ID = "OctopusSlapping";
    private static final String IMG_PATH = "img/cards/undertaker/OctopusSlapping.png";
    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DAMAGE = 4;
    private static final int POISE_BREAK = 1;
    private static final int SELF_WEAK = 2;

    public OctopusSlapping() {
        super(ID, strings().NAME, IMG_PATH, COST, strings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = POISE_BREAK;
        refreshDescription();
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }

        // Judge against the incoming damage and current block, before special damage prevention resolves.
        boolean penetratesBlock = this.damage > Math.max(0, m.currentBlock);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (penetratesBlock) {
            addToBot(new ApplyPowerAction(m, p,
                    new PoiseBreakPower(m, this.magicNumber), this.magicNumber));
        } else {
            addToBot(new ApplyPowerAction(p, p,
                    new WeakPower(p, SELF_WEAK, false), SELF_WEAK));
        }
    }

    private void refreshDescription() {
        String description = strings().DESCRIPTION
                .replace("!M!", Integer.toString(this.magicNumber))
                .replace("!MS!", Integer.toString(SELF_WEAK));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OctopusSlapping();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(1);
            refreshDescription();
        }
    }
}
