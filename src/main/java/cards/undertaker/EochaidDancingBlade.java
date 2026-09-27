package cards.undertaker;

import actions.EochaidDancingBladeDrawAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class EochaidDancingBlade extends CustomCard {
    public static final String ID = "EochaidDancingBlade";
    private static final String IMG_PATH = "img/cards/undertaker/EochaidDancingBlade.png";

    public EochaidDancingBlade() {
        super(ID, getCardStrings().NAME, IMG_PATH, 1, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 7;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new EochaidDancingBladeDrawAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new EochaidDancingBlade();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }
}
