package cards.revenant;

import actions.MischiefAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;

public class Mischief extends CustomCard {
    public static final String ID = "Mischief";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Mischief() {
        super(ID, STRINGS.NAME, "img/cards/revenant/Mischief.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public boolean canUse(AbstractPlayer player, AbstractMonster monster) {
        if (!super.canUse(player, monster)) return false;
        if (!AbstractSummonPower.hasSummonPower(player)) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = (AbstractSummonPower.hasSummonPower(AbstractDungeon.player)
                ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR).cpy();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new MischiefAction(player, magicNumber));
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
        return new Mischief();
    }
}
