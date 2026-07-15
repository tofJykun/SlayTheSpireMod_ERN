package cards.revenant;

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
import powers.SummonFrederick;
import powers.SummonHelen;
import powers.SummonSebastian;

public class Invoke extends CustomCard {
    public static final String ID = "Invoke";
    private static final String IMG_PATH = "img/cards/revenant/Invoke.png";
    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public Invoke() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.BASIC, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                getRandomBasicSummon(p), 1));
    }

    private AbstractPower getRandomBasicSummon(AbstractPlayer player) {
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                return new SummonHelen((AbstractCreature)player);
            case 1:
                return new SummonFrederick((AbstractCreature)player);
            default:
                return new SummonSebastian((AbstractCreature)player);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Invoke();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}
