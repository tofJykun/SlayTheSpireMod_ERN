package cards.guardian;

import actions.ReplaceAttackWithBlockAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.CombatState;
import patches.AbstractCardEnum;

public class LessLikelyToBeTargeted extends CustomCard {
    public static final String ID = "LessLikelyToBeTargeted";

    public LessLikelyToBeTargeted() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/guardian/LessLikelyToBeTargeted.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 99;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractRoom room = CombatState.currentRoom();
        if (room != null && room.monsters != null) {
            for (AbstractMonster monster : room.monsters.monsters) {
                addToBot(new ReplaceAttackWithBlockAction(monster, p, magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LessLikelyToBeTargeted();
    }
}
