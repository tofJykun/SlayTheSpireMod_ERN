package cards.recluse;

import actions.GlintbladePhalanxAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class GlintbladePhalanx extends CustomCard {
    public static final String ID = "GlintbladePhalanx";
    private static final String IMG_PATH = "img/cards/recluse/GlintbladePhalanx.png";
    private static final int COST = -1;
    private static final int DAMAGE = 5;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS = 2;

    public GlintbladePhalanx() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseBlock = BLOCK;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GlintbladePhalanxAction(p, m, this.damage, this.block,
                this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GlintbladePhalanx();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS);
            upgradeBlock(UPGRADE_PLUS);
        }
    }
}
