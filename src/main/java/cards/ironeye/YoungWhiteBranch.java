package cards.ironeye;

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
import powers.YoungWhiteBranchPower;

public class YoungWhiteBranch extends CustomCard {
    public static final String ID = "YoungWhiteBranch";
    private static final String IMG_PATH = "img/cards/ironeye/YoungWhiteBranch.png";

    public YoungWhiteBranch() {
        super(ID, strings().NAME, IMG_PATH, 2, strings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    private static CardStrings strings() { return CardCrawlGame.languagePack.getCardStrings(ID); }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction(p, p,
                new YoungWhiteBranchPower((AbstractCreature)p, this.upgraded), 1));
    }

    @Override public AbstractCard makeCopy() { return new YoungWhiteBranch(); }

    @Override public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = strings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
