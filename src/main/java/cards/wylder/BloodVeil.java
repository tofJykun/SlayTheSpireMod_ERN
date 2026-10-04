package cards.wylder;

import basemod.abstracts.CustomCard;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import patches.AbstractCardEnum;

public class BloodVeil extends CustomCard {
    public static final String ID = "BloodVeil";
    private static final String IMG_PATH = "img/cards/wylder/BloodVeil.png";
    private static final int COST = 0;
    private static final int SELF_FRAIL = 2;
    private static final int ENEMY_VULNERABLE = 5;

    public BloodVeil() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.baseMagicNumber = this.magicNumber = ENEMY_VULNERABLE;
        this.cardsToPreview = new Wound();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getSelfFrail() { return SELF_FRAIL; }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new FrailPower((AbstractCreature)p, SELF_FRAIL, false), SELF_FRAIL));
        if (m != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new VulnerablePower((AbstractCreature)m, magicNumber, false), magicNumber));
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)p,
                    new WeakPower((AbstractCreature)m, magicNumber, false), magicNumber));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int missing = Math.max(0, BaseMod.MAX_HAND_SIZE - p.hand.size());
                if (missing > 0) addToTop(new MakeTempCardInHandAction(new Wound(), missing));
                isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodVeil();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
