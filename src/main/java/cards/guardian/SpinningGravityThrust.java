package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.HoverPower;

public class SpinningGravityThrust extends CustomCard {
    public static final String ID = "SpinningGravityThrust";
    private static final String IMG_PATH = "img/cards/guardian/SpinningGravityThrust.png";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int HOVER_LOSS = 2;

    public SpinningGravityThrust() {
        super(ID, STRINGS.NAME, IMG_PATH, 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 15;
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public int getHoverLoss() {
        return HOVER_LOSS;
    }

    @Override
    public boolean canUse(AbstractPlayer player, AbstractMonster monster) {
        if (!super.canUse(player, monster)) {
            return false;
        }
        if (player == null || !player.hasPower(HoverPower.POWER_ID)
                || player.getPower(HoverPower.POWER_ID).amount < HOVER_LOSS) {
            this.cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DrawCardAction(player, this.magicNumber));
        addToBot(new ReducePowerAction(player, player, HoverPower.POWER_ID, HOVER_LOSS));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpinningGravityThrust();
    }
}
