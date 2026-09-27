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

public class Marking extends CustomCard {
    public static final String ID = "Marking";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/Marking.png";
    private static final int COST = 1;
    private int healthLoss = 5;

    public Marking() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.BASIC, CardTarget.ENEMY);
        this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = MarkingPower.DAMAGE_THRESHOLD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m, p, new MarkingPower(m, p, this.healthLoss), this.healthLoss));
    }

    public int getHealthLoss() {
        return this.healthLoss;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.healthLoss += 5;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Marking();
    }
}
