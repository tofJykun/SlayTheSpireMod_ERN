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

public class NoxFlowingShieldTypeP extends CustomCard {
    public static final String ID = "NoxFlowingShieldTypeP";
    private static final String IMG_PATH = "img/cards/wylder/NoxFlowingShieldTypeP.png";
    private static final int COST = 0;
    private static final int BLOCK_AMT = 4;
    private static final int DEXTERITY_LOSS = 2;
    private static final int UPGRADE_PLUS_DEXTERITY = -1;

    public NoxFlowingShieldTypeP() {
        this(true);
    }

    NoxFlowingShieldTypeP(boolean includePreview) {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_AMT;
        this.baseMagicNumber = DEXTERITY_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.isEthereal = true;
        if (includePreview) {
            this.cardsToPreview = new NoxFlowingShieldTypeI(false);
        }
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(makePairedCard(), 1));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new DexterityPower((AbstractCreature)p, -this.magicNumber), -this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new GainDexterityAtEndOfTurnPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    private AbstractCard makePairedCard() {
        AbstractCard card = new NoxFlowingShieldTypeI();
        if (this.upgraded) {
            card.upgrade();
        }
        return card;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NoxFlowingShieldTypeP();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEXTERITY);
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
            if (this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
        }
    }
}
