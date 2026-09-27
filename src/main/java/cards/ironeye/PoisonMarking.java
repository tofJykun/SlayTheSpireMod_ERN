package cards.ironeye;

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
import powers.MarkingPower;
import powers.PoisonMarkingPower;

public class PoisonMarking extends CustomCard {
    public static final String ID = "PoisonMarking";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/PoisonMarking.png";
    private static final int COST = 1;
    private int poison = 10;

    public PoisonMarking() {
        super(ID, STRINGS.NAME, IMG_PATH, COST, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = MarkingPower.DAMAGE_THRESHOLD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p, new PoisonMarkingPower(m, p, this.poison), this.poison));
    }

    public int getPoison() {
        return this.poison;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.poison += 2;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PoisonMarking();
    }
}
