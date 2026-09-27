package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;

public class DuelingShield extends CustomCard {
    public static final String ID = "DuelingShield";
    private static final String IMG_PATH = "img/cards/guardian/DuelingShield.png";
    private static final int COST = 2;
    private static final int BLOCK = 12;
    private static final int STRENGTH = 3;
    private static final int ENEMY_STRENGTH = 1;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int UPGRADE_PLUS_STRENGTH = 1;

    public DuelingShield() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = STRENGTH;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StrengthPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new StrengthPower((AbstractCreature)m, ENEMY_STRENGTH), ENEMY_STRENGTH));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DuelingShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH);
        }
    }
}
