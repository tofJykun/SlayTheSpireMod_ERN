package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import patches.AbstractCardEnum;

public class DungPie extends CustomCard {
    public static final String ID = "DungPie";
    private static final String IMG_PATH = "img/cards/raider/DungPie.png";
    private static final int COST = 1;
    private static final int SELF_POISON = 3;
    private static final int POISON = 12;
    private static final int UPGRADE_PLUS_POISON = 4;

    public DungPie() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseMagicNumber = POISON;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new PoisonPower((AbstractCreature)p, (AbstractCreature)p, SELF_POISON), SELF_POISON,
                AbstractGameAction.AttackEffect.POISON));
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new PoisonPower((AbstractCreature)m, (AbstractCreature)p, this.magicNumber), this.magicNumber,
                    AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DungPie();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
        }
    }
}
