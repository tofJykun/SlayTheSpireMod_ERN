package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class MoonlightGreatsword extends CustomCard {
    public static final String ID = "MoonlightGreatsword";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public MoonlightGreatsword() {
        super(ID, STRINGS.NAME, "img/cards/revenant/MoonlightGreatsword.png", 0, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 3;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        addToBot(new RemoveDebuffsAction(p));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (EnergyPanel.totalCount != 0) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = EnergyPanel.totalCount == 0
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new MoonlightGreatsword();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
