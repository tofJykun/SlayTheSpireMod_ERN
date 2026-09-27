package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class LamentingVisage extends CustomCard {
    public static final String ID = "LamentingVisage";
    private static final String IMG_PATH = "img/cards/undertaker/LamentingVisage.png";
    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DAMAGE = 8;

    public LamentingVisage() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.COMMON,
                CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        addToBot((AbstractGameAction)new DiscardPileToTopOfDeckAction((AbstractCreature)p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LamentingVisage();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
