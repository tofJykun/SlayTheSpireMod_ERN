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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;
import powers.FadingSpiritPower;
import powers.SpiritPower;

public class GhizaWheel extends CustomCard {
    public static final String ID = "GhizaWheel";
    private static final String IMG_PATH = "img/cards/revenant/GhizaWheel.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int SPIRIT = 3;
    private static final int DEBUFF = 2;

    public GhizaWheel() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = DEBUFF;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new SpiritPower((AbstractCreature)p, SPIRIT), SPIRIT));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new FadingSpiritPower((AbstractCreature)p, SPIRIT), SPIRIT));
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new WeakPower((AbstractCreature)monster, this.magicNumber, false), this.magicNumber));
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new VulnerablePower((AbstractCreature)monster, this.magicNumber, false), this.magicNumber));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhizaWheel();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
