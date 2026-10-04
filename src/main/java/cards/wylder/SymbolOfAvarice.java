package cards.wylder;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SymbolOfAvarice extends CustomCard {
    public static final String ID = "SymbolOfAvarice";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/wylder/SymbolOfAvarice.png";
    private static final int COST = 0;
    private static final int SELF_DAMAGE = 6;
    private static final int ENERGY = 2;
    private static final int UPGRADED_ENERGY = 3;

    public SymbolOfAvarice() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = SELF_DAMAGE;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)p,
                new DamageInfo((AbstractCreature)p, this.magicNumber, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.FIRE));
        addToBot((AbstractGameAction)new GainEnergyAction(this.upgraded ? UPGRADED_ENERGY : ENERGY));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SymbolOfAvarice();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
