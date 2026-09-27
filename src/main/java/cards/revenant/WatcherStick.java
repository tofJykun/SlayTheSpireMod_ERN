package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WatcherStick extends CustomCard {
    public static final String ID = "WatcherStick";
    private static final String IMG_PATH = "img/cards/revenant/WatcherStick.png";
    private static final int DAMAGE = 50;
    private static final int UPGRADE_PLUS_DAMAGE = 22;

    public WatcherStick() {
        super(ID, getCardStrings().NAME, IMG_PATH, 0, getCardStrings().DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }
    private static CardStrings getCardStrings() { return CardCrawlGame.languagePack.getCardStrings(ID); }
    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
    @Override public AbstractCard makeCopy() { return new WatcherStick(); }
    @Override public void upgrade() { if (!this.upgraded) { upgradeName(); upgradeDamage(UPGRADE_PLUS_DAMAGE); } }
}
