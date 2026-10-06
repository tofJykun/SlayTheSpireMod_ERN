package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import general.CombatState;
import patches.AbstractCardEnum;
import powers.ConciliationPower;
import actions.ConciliationAction;

public class Conciliation extends CustomCard {
    public static final String ID = "Conciliation";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Conciliation() {
        super(ID, STRINGS.NAME, "img/cards/revenant/Conciliation.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 20;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractRoom room = CombatState.currentRoom();
        if (ConciliationAction.isEligible(room)) {
            addToBot(new ApplyPowerAction(p, p, new ConciliationPower(p, room, magicNumber), 0));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(-5);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Conciliation();
    }
}
