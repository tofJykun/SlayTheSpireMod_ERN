package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.GainSpiritAtEndOfTurnPower;
import powers.SpiritPower;

public class SpiritGeyser extends CustomCard {
    public static final String ID = "SpiritGeyser";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public SpiritGeyser() {
        super(ID, STRINGS.NAME, "img/cards/revenant/SpiritGeyser.png", 2, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 20;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));
        final int loss = magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                // Check Artifact when the loss resolves, not before the preceding attack.
                if (!p.hasPower("Artifact")) {
                    addToTop(new ApplyPowerAction(p, p, new GainSpiritAtEndOfTurnPower(p, loss), loss));
                }
                addToTop(new ApplyPowerAction(p, p, new SpiritPower(p, -loss), -loss));
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(8);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpiritGeyser();
    }
}
