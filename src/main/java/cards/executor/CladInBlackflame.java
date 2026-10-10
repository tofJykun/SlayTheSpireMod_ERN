package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import general.CombatState;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

public class CladInBlackflame extends CustomCard {
    public static final String ID = "CladInBlackflame";

    public CladInBlackflame() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/CladInBlackflame.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 0;
        this.exhaust = true;
    }

    private int totalBloodburn() {
        if (!CombatState.isInCombat() || AbstractDungeon.getMonsters() == null) {
            return 0;
        }
        long total = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped() || monster.halfDead || monster.currentHealth <= 0) {
                continue;
            }
            AbstractPower bloodburn = monster.getPower(BloodburnPower.POWER_ID);
            if (bloodburn != null) {
                total += Math.max(0, bloodburn.amount);
            }
        }
        return (int)Math.min(Integer.MAX_VALUE, total);
    }

    @Override
    public void applyPowers() {
        this.baseBlock = totalBloodburn();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        this.baseBlock = totalBloodburn();
        super.calculateCardDamage(monster);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        applyPowers();
        addToBot(new GainBlockAction(player, player, this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CladInBlackflame();
    }
}
