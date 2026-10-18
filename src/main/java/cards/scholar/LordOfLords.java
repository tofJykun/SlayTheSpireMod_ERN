package cards.scholar;

import actions.LordOfLordsAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

@NoPools
public class LordOfLords extends CustomCard {
    public static final String ID = "LordOfLords";

    public LordOfLords() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/LordOfLords.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 33;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LordOfLordsAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(1); }
    }

    @Override
    public AbstractCard makeCopy() { return new LordOfLords(); }
}
