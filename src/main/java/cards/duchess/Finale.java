package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import patches.AbstractCardEnum;
import patches.GetLostPatch;
import powers.GetLostPower;

public class Finale extends CustomCard {
    public static final String ID = "Finale";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Finale() {
        super(ID, STRINGS.NAME, "img/cards/duchess/Finale.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                GetLostPatch.enableEndOfTurn(p);
            }
        });
        if (!CombatState.isInCombat()) return;
        for (AbstractMonster monster : CombatState.currentRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, p, new GetLostPower(monster), 30));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Finale(); }
}
