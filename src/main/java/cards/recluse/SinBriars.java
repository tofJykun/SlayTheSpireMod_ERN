package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SinBriarsPower;

public class SinBriars extends CustomCard {
    public static final String ID = "SinBriars";
    private static final String IMG_PATH = "img/cards/recluse/SinBriars.png";
    private static final int COST = 2;
    private static final int BLOCK = 15;
    private static final int UPGRADE_PLUS_BLOCK = 5;
    private static final int BLOODLOSS = 1;
    private static final int UPGRADE_PLUS_BLOODLOSS = 1;

    public SinBriars() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = BLOODLOSS;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SinBriarsPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SinBriars();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_BLOODLOSS);
        }
    }
}
