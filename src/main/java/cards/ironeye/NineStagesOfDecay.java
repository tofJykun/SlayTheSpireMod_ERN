package cards.ironeye;

import actions.NineStagesOfDecayAction;
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
import powers.NineStagesOfDecayPower;

public class NineStagesOfDecay extends CustomCard {
    public static final String ID = "NineStagesOfDecay";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public NineStagesOfDecay() {
        super(ID, STRINGS.NAME, "img/cards/ironeye/NineStagesOfDecay.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractRoom room = CombatState.currentRoom();
        if (NineStagesOfDecayAction.isEligible(room)) {
            addToBot(new ApplyPowerAction(p, p, new NineStagesOfDecayPower(p, room), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NineStagesOfDecay();
    }
}
