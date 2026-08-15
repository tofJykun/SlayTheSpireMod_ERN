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
import patches.AbstractCardEnum;
import powers.BloodlossPower;

public class RiversOfBlood extends CustomCard {
    public static final String ID = "RiversOfBlood";
    private static final String IMG_PATH = "img/cards/executor/RiversOfBlood.png";
    private static final int COST = 1;
    private static final int ENEMY_BLOODLOSS = 5;
    private static final int SELF_BLOODLOSS = 3;
    private static final int UPGRADE_REDUCE_SELF_BLOODLOSS = 1;

    public RiversOfBlood() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = SELF_BLOODLOSS;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new BloodlossPower((AbstractCreature)monster, ENEMY_BLOODLOSS),
                        ENEMY_BLOODLOSS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new BloodlossPower((AbstractCreature)p, this.magicNumber),
                this.magicNumber, AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RiversOfBlood();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(-UPGRADE_REDUCE_SELF_BLOODLOSS);
        }
    }
}
