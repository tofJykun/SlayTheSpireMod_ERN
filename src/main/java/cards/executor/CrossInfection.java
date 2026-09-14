package cards.executor;

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
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.InfectionStrikePower;

import java.util.ArrayList;

public class CrossInfection extends CustomCard {
    public static final String ID = "CrossInfection";
    private static final String IMG_PATH = "img/cards/executor/CrossInfection.png";
    private static final int COST = 2;

    public CrossInfection() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) {
            return;
        }

        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        for (AbstractPower power : m.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffs.add(power);
            }
        }

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster == m || monster.isDeadOrEscaped()) {
                continue;
            }
            for (AbstractPower debuff : debuffs) {
                AbstractPower duplicate = InfectionStrikePower.makeDuplicatePower(debuff,
                        (AbstractCreature)monster, (AbstractCreature)p, debuff.amount);
                if (duplicate != null) {
                    addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster,
                            (AbstractCreature)p, duplicate, debuff.amount));
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrossInfection();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}
