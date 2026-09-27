package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.SpinningWeaponPower;

public class SpinningWeapon extends CustomCard {
    public static final String ID = "SpinningWeapon";
    private static final String IMG_PATH = "img/cards/undertaker/SpinningWeapon.png";

    public SpinningWeapon() {
        super(ID, strings().NAME, IMG_PATH, 1, strings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 16;
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SpinningWeaponPower(p, this.block), this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpinningWeapon();
    }
}
