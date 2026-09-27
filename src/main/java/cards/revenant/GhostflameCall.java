package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.AbstractSummonPower;
import powers.FrostbitePower;
import powers.SummonFrederick;
import powers.SummonHelen;
import powers.SummonSebastian;

public class GhostflameCall extends CustomCard {
    public static final String ID = "GhostflameCall";
    private static final String IMG_PATH = "img/cards/revenant/GhostflameCall.png";

    public GhostflameCall() {
        super(ID, getCardStrings().NAME, IMG_PATH, 1, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractSummonPower.hasSummonPower(p)) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(monster, p,
                            new FrostbitePower(monster, this.magicNumber), this.magicNumber));
                }
            }
        } else {
            addToBot(new ApplyPowerAction(p, p, getRandomBasicSummon(p), 1));
        }
    }

    private AbstractPower getRandomBasicSummon(AbstractPlayer p) {
        switch (AbstractDungeon.cardRandomRng.random(2)) {
            case 0:
                return new SummonHelen(p);
            case 1:
                return new SummonFrederick(p);
            default:
                return new SummonSebastian(p);
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhostflameCall();
    }
}
