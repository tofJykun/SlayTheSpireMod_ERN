package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingBody;
import patches.AbstractCardEnum;
import patches.ReplayField;
import powers.HornetRingPower;

public class Unveil extends CustomCard {
    public static final String ID = "Unveil";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public Unveil() {
        super(ID, STRINGS.NAME, "img/cards/duchess/Unveil.png", 0, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMagicNumber = this.magicNumber = 1;
        ReplayField.setReplay(this, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ReplayField.setReplayAllowedForUse(SmithingBody.physical(this),
                m != null && HornetRingPower.isAttackIntent(m.intent));
        addToBot(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            ReplayField.setReplay(this, 2);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unveil();
    }
}
