package cards.scholar;

import actions.EmeraldTabletDiscoveryAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.OperationeSolis;

public class EmeraldTablet extends CustomCard {
    public static final String ID = "EmeraldTablet";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public EmeraldTablet() {
        super(ID, STRINGS.NAME, "img/cards/scholar/EmeraldTablet.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new EmeraldTabletDiscoveryAction(upgraded));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // Read the live stage only after discovery and its quest callbacks finish.
                OperationeSolis.obtain(p).showCurrentStageText();
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EmeraldTablet();
    }
}
