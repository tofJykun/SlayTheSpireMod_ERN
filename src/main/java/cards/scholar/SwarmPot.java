package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ReplayField;
import powers.BloodlossPower;

public class SwarmPot extends CustomCard {
    public static final String ID = "SwarmPot";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SwarmPot() {
        super(ID, STRINGS.NAME, "img/cards/scholar/SwarmPot.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 4;
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, p,
                        new BloodlossPower(monster, magicNumber), magicNumber));
            }
        }
    }

    @Override public boolean canUpgrade() { return timesUpgraded < 2; }

    @Override public void upgrade() {
        if (!canUpgrade()) return;
        upgradeName();
        if (timesUpgraded == 1) {
            upgradeMagicNumber(1);
        } else {
            ReplayField.setReplay(this, 1);
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
        }
        initializeDescription();
    }

    @Override public AbstractCard makeCopy() { return new SwarmPot(); }
}
