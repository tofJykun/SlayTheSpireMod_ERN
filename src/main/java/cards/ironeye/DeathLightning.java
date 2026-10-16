package cards.ironeye;

import actions.DeathLightningAction;
import basemod.abstracts.CustomCard;
import cards.tempcards.LotteryTicket;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class DeathLightning extends CustomCard {
    public static final String ID = "DeathLightning";

    public DeathLightning() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/ironeye/DeathLightning.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Ironeye_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 20;
        baseMagicNumber = magicNumber = 3;
        cardsToPreview = new LotteryTicket();
        cardsToPreview.upgrade();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new DeathLightningAction(player, monster,
                new DamageInfo(player, damage, damageTypeForTurn), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(5);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeathLightning();
    }
}
