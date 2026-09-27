package cards.executor;

import actions.TriggerEnemyAnomaliesAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ScarletRotPower;

public class ScarletAeonia extends CustomCard {
    public static final String ID = "ScarletAeonia";
    private static final String IMG_PATH = "img/cards/executor/ScarletAeonia.png";
    private static final int COST = 3;
    private static final int SCARLET_ROT = 9;

    public ScarletAeonia() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = SCARLET_ROT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new ScarletRotPower((AbstractCreature)monster, (AbstractCreature)p, this.magicNumber),
                        this.magicNumber, AbstractGameAction.AttackEffect.POISON));
            }
        }
        addToBot(new TriggerEnemyAnomaliesAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new ScarletAeonia();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
