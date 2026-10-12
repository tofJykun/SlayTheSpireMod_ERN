package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.FragileField;
import powers.MushroomPower;

public class CrispyBread extends CustomCard {
    public static final String ID = "CrispyBread";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrispyBread() {
        super(ID, STRINGS.NAME, "img/cards/duchess/CrispyBread.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 10;
        this.baseMagicNumber = this.magicNumber = 3;
        FragileField.setBase(this, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new MushroomPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(6);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrispyBread();
    }
}
