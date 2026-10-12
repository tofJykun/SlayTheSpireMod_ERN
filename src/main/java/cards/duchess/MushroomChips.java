package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;
import patches.ReplayField;
import powers.MushroomPower;

public class MushroomChips extends CustomCard {
    public static final String ID = "MushroomChips";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public MushroomChips() {
        super(ID, STRINGS.NAME, "img/cards/duchess/MushroomChips.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 3;
        ReplayField.setReplay(this, 1);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p == null || !super.canUse(p, m)) return false;
        if (!p.hasPower(MushroomPower.POWER_ID)) {
            this.cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            ReplayField.setReplay(this, 2);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MushroomChips();
    }
}
