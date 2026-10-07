package cards.guardian;

import actions.StarPiercerAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;

public class StarPiercer extends CustomCard {
    public static final String ID = "StarPiercer";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public StarPiercer() {
        super(ID, STRINGS.NAME, "img/cards/guardian/StarPiercer.png", 2, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 7;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int vigor = p.hasPower(VigorPower.POWER_ID)
                ? Math.max(0, p.getPower(VigorPower.POWER_ID).amount) : 0;
        addToBot(new StarPiercerAction(p, m, this,
                new DamageInfo(p, this.damage, this.damageTypeForTurn), vigor));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StarPiercer();
    }
}
