package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import patches.AbstractCardEnum;
import powers.PerfectGuardPower;

public class CurseWardGreatshield extends CustomCard {
    public static final String ID = "CurseWardGreatshield";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CurseWardGreatshield() {
        super(ID, STRINGS.NAME, "img/cards/guardian/CurseWardGreatshield.png", 2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 20;
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    public int getArtifactAmount() {
        return upgraded ? 2 : 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new PerfectGuardPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, getArtifactAmount()), getArtifactAmount()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(5);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CurseWardGreatshield();
    }
}
