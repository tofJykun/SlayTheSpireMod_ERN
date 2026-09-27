package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import patches.AbstractCardEnum;

public class OnyxLordsRepulsion extends CustomCard {
    public static final String ID = "OnyxLordsRepulsion";
    public static final int HP_LOSS = 6;

    public OnyxLordsRepulsion() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/executor/OnyxLordsRepulsion.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new LoseHPAction(monster, p, HP_LOSS));
            }
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.halfDead && monster.currentHealth > 0) {
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, magicNumber, false), magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OnyxLordsRepulsion();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }
}
