package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.CrystalRingShieldPower;

public class CrystalRingShield extends CustomCard {
    public static final String ID = "CrystalRingShield";
    private static final String IMG_PATH = "img/cards/recluse/CrystalRingShield.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 20;
    private static final int UPGRADE_PLUS_BLOCK = 8;

    public CrystalRingShield() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_AMT;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p,
                new CrystalRingShieldPower(p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrystalRingShield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
