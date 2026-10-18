package cards.scholar;

import actions.GainGoldWithAnimationAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

@NoPools
public class Empyrean extends CustomCard {
    public static final String ID = "Empyrean";

    public Empyrean() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/Empyrean.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 7;
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        final int rewardAmount = magicNumber;
        addToBot(new GainGoldWithAnimationAction(rewardAmount));
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                if (!p.isDead) p.increaseMaxHp(rewardAmount, true);
                isDone = true;
            }
        });
    }

    @Override public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override public AbstractCard makeCopy() { return new Empyrean(); }
}
