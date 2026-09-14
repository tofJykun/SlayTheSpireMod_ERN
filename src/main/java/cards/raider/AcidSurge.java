package cards.raider;

import actions.AcidSurgeAction;
import basemod.abstracts.CustomCard;
import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class AcidSurge extends CustomCard {
    public static final String ID = "AcidSurge";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/raider/AcidSurge.png";
    private static final int COST = 1;
    private static final int STRENGTH_LOSS = 1;
    private static final int UPGRADE_PLUS_STRENGTH_LOSS = 1;

    public AcidSurge() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseMagicNumber = STRENGTH_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new CraftmanCreation();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new AcidSurgeAction(p, m, this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        if (!hasCraftmanCreationInHand()) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            return false;
        }
        return true;
    }

    private boolean hasCraftmanCreationInHand() {
        if (AbstractDungeon.player == null) {
            return false;
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof CraftmanCreation) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new AcidSurge();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH_LOSS);
        }
    }
}
