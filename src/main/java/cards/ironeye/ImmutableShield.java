package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import patches.AbstractCardEnum;
import powers.VirtualCurrencyPower;

public class ImmutableShield extends CustomCard {
    public static final String ID = "ImmutableShield";

    public ImmutableShield() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/ironeye/ImmutableShield.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 2;
        this.baseBlock = this.block = 2;
    }

    private int currentBaseBlock() {
        AbstractPower tokens = CombatState.isInCombat() && AbstractDungeon.player != null
                ? AbstractDungeon.player.getPower(VirtualCurrencyPower.POWER_ID) : null;
        return (int)Math.min(Integer.MAX_VALUE,
                (long)this.magicNumber + (tokens == null ? 0 : Math.max(0, tokens.amount)));
    }

    @Override
    public void applyPowers() {
        this.baseBlock = currentBaseBlock();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        this.baseBlock = currentBaseBlock();
        super.calculateCardDamage(monster);
    }

    @Override
    public void update() {
        super.update();
        // Spending part of a Token stack does not always call onModifyPower.
        if (this.baseBlock != currentBaseBlock()) {
            if (CombatState.isInCombat() && AbstractDungeon.player != null) {
                applyPowers();
            } else {
                this.baseBlock = this.block = currentBaseBlock();
                this.isBlockModified = false;
            }
        }
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        applyPowers();
        addToBot(new GainBlockAction(player, player, this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
            upgradeBlock(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ImmutableShield();
    }
}
