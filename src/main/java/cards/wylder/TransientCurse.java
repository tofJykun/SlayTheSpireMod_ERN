package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.TransientCursePower;

public class TransientCurse extends CustomCard {
    public static final String ID = "TransientCurse";
    private static final String IMG_PATH = "img/cards/wylder/Defend.png";
    private static final int COST = 0;
    private static final int DEXTERITY_LOSS = 3;
    private static final int STRENGTH_GAIN = 4;
    private static final int UPGRADE_PLUS_STRENGTH = 2;

    public TransientCurse() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = STRENGTH_GAIN;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new DexterityPower((AbstractCreature)p, -DEXTERITY_LOSS), -DEXTERITY_LOSS));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StrengthPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new TransientCursePower((AbstractCreature)p, DEXTERITY_LOSS, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new TransientCurse();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH);
        }
    }
}
