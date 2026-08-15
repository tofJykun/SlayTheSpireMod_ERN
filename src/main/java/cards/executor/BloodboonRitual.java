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
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.BloodlossPower;

public class BloodboonRitual extends CustomCard {
    public static final String ID = "BloodboonRitual";
    private static final String IMG_PATH = "img/cards/executor/BloodboonRitual.png";
    private static final int COST = -1;

    public BloodboonRitual() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = getXValue(p);
        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                                new BloodlossPower((AbstractCreature)monster, effect),
                                effect, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                }
            }
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }

    private int getXValue(AbstractPlayer p) {
        int value = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            value = this.energyOnUse;
        }
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
            p.getRelic("Chemical X").flash();
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodboonRitual();
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
