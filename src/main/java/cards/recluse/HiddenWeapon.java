package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BedOfMagicCostReductionPower;

public class HiddenWeapon extends CustomCard {
    public static final String ID = "HiddenWeapon";
    private static final String IMG_PATH = "img/cards/recluse/HiddenWeapon.png";
    private static final int COST = 1;
    private static final int ATTACK_COUNT = 2;
    private static final int UPGRADE_PLUS_ATTACK_COUNT = 1;

    public HiddenWeapon() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = ATTACK_COUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new BedOfMagicCostReductionPower((AbstractCreature)p, CardType.ATTACK, this.magicNumber),
                this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HiddenWeapon();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ATTACK_COUNT);
        }
    }
}
