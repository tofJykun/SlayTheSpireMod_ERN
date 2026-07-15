package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import patches.AbstractCardEnum;
import powers.GainDexterityAtEndOfTurnPower;

public class NoxFlowingShieldTypeI extends CustomCard {
    public static final String ID = "NoxFlowingShieldTypeI";
    private static final String IMG_PATH = "img/cards/wylder/NoxFlowingShieldTypeI.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 4;
    private static final int DEXTERITY_LOSS = 1;

    public NoxFlowingShieldTypeI() {
        this(true);
    }

    NoxFlowingShieldTypeI(boolean includePreview) {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
        if (includePreview) {
            this.cardsToPreview = new NoxFlowingShieldTypeP(false);
        }
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(makePairedCard(), 1));
        if (!this.upgraded) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new DexterityPower((AbstractCreature)p, -DEXTERITY_LOSS), -DEXTERITY_LOSS));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new GainDexterityAtEndOfTurnPower((AbstractCreature)p, DEXTERITY_LOSS), DEXTERITY_LOSS));
        }
    }

    private AbstractCard makePairedCard() {
        AbstractCard card = new NoxFlowingShieldTypeP();
        if (this.upgraded) {
            card.upgrade();
        }
        return card;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NoxFlowingShieldTypeI();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
            if (this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
        }
    }
}
