package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.EternalSleepPower;

public class PowerOfTheLightlessVoid extends CustomCard {
    public static final String ID = "PowerOfTheLightlessVoid";

    public PowerOfTheLightlessVoid() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/undertaker/PowerOfTheLightlessVoid.png", 1,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Undertaker_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && !monster.isDying && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, p, new EternalSleepPower(monster), 1));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerOfTheLightlessVoid();
    }
}
