package cards.revenant;

import actions.HorizontalAllianceAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class HorizontalAlliance extends CustomCard {
    public static final String ID = "HorizontalAlliance";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public HorizontalAlliance() {
        super(ID, STRINGS.NAME, "img/cards/revenant/HorizontalAlliance.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (!HorizontalAllianceAction.hasSlash(p)) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = HorizontalAllianceAction.hasSlash(AbstractDungeon.player)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HorizontalAllianceAction(p, magicNumber, STRINGS.EXTENDED_DESCRIPTION[1]));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HorizontalAlliance();
    }
}
