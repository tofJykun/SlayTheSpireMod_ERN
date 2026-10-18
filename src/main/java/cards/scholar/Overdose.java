package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.BulkPotionQueue;
import patches.AbstractCardEnum;
import patches.FragileField;

public class Overdose extends CustomCard {
    public static final String ID = "Overdose";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Overdose() {
        super(ID, STRINGS.NAME, "img/cards/scholar/Overdose.png", 2, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 40;
        baseMagicNumber = magicNumber = 2;
        isEthereal = true;
        FragileField.setBase(this, 3);
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ObtainPotionAction(BulkPotionQueue.getRandomBulkPotion()));
        }
    }

    @Override public boolean canUpgrade() { return true; }

    @Override public void upgrade() {
        timesUpgraded++;
        upgraded = true;
        name = STRINGS.NAME + "+" + timesUpgraded;
        initializeTitle();
        int next = (int)Math.min(Integer.MAX_VALUE, (long)FragileField.state(this).base + timesUpgraded);
        FragileField.upgradeBase(this, next);
        initializeDescription();
    }

    @Override public AbstractCard makeCopy() { return new Overdose(); }
}
