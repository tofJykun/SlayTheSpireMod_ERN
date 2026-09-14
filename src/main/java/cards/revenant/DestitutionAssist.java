package cards.revenant;

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
import powers.DestitutionAssistPower;

public class DestitutionAssist extends CustomCard {
    public static final String ID = "DestitutionAssist";
    private static final String IMG_PATH = "img/cards/revenant/DestitutionAssist.png";
    private static final int COST = 1;
    private static final int BLOCK_GAIN = 4;
    private static final int UPGRADE_PLUS_BLOCK_GAIN = 2;

    public DestitutionAssist() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = BLOCK_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new DestitutionAssistPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DestitutionAssist();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK_GAIN);
        }
    }
}
