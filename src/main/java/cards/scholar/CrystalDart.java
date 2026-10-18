package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CrystalDartCombat;
import patches.AbstractCardEnum;
import patches.FragileField;
import powers.HornetRingPower;

public class CrystalDart extends CustomCard {
    public static final String ID = "CrystalDart";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public CrystalDart() {
        super(ID, STRINGS.NAME, "img/cards/scholar/CrystalDart.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 10;
        baseMagicNumber = magicNumber = 5;
        FragileField.setBase(this, 5);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean redirect = m != null && HornetRingPower.isAttackIntent(m.intent);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (redirect) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    CrystalDartCombat.apply(m, p);
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
        }
    }

    @Override
    public AbstractCard makeCopy() { return new CrystalDart(); }
}
