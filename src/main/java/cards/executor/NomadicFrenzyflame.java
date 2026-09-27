package cards.executor;

import actions.NomadicFrenzyflameAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.PlayerDebuffStats;
import patches.AbstractCardEnum;

public class NomadicFrenzyflame extends CustomCard {
    public static final String ID = "NomadicFrenzyflame";

    public NomadicFrenzyflame() {
        super(ID, strings().NAME, "img/cards/executor/NomadicFrenzyflame.png", 1,
                strings().DESCRIPTION, CardType.SKILL, AbstractCardEnum.Executor_COLOR,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = 2;
        refreshDescription();
    }

    private static CardStrings strings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    private void refreshDescription() {
        String description = strings().DESCRIPTION + strings().EXTENDED_DESCRIPTION[0]
                + PlayerDebuffStats.getApplications() + strings().EXTENDED_DESCRIPTION[1];
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = PlayerDebuffStats.getApplications();
        for (int i = 0; i < count; i++) {
            addToBot(new NomadicFrenzyflameAction(p, this.magicNumber));
        }
    }

    @Override
    public void update() {
        super.update();
        refreshDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        refreshDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NomadicFrenzyflame();
    }
}
