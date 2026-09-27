package cards.raider;

import actions.DenialAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Boomerang extends CustomCard {
    public static final String ID = "Boomerang";
    private static final String IMG_PATH = "img/cards/raider/Boomerang.png";
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    public Boomerang() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        addToBot((AbstractGameAction)new DenialAction(1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Boomerang();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.exhaust = false;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
