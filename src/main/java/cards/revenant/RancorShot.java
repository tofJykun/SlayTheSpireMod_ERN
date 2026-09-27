package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class RancorShot extends CustomCard {
    public static final String ID = "RancorShot";

    public RancorShot() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/revenant/RancorShot.png", 0,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    private void setDamageFromBlock() {
        int block = AbstractDungeon.player == null ? 0 : Math.max(0, AbstractDungeon.player.currentBlock);
        this.baseDamage = block * this.magicNumber;
    }

    @Override
    public void applyPowers() {
        setDamageFromBlock();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        setDamageFromBlock();
        super.calculateCardDamage(m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new RemoveAllBlockAction(p, p));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = 0;
    }

    @Override
    public void resetAttributes() {
        this.baseDamage = 0;
        super.resetAttributes();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RancorShot();
    }
}
