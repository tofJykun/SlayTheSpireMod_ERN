package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SpearcallRitual extends CustomCard {
    public static final String ID = "SpearcallRitual";
    private AbstractMonster previewTarget;

    public SpearcallRitual() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/guardian/SpearcallRitual.png", 2,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 30;
        exhaust = true;
    }

    private int hitCount(AbstractMonster monster) {
        return 1 + (monster == null ? 0 : Math.max(0, monster.currentHealth)) / Math.max(1, magicNumber);
    }

    public int getHitCount() {
        return hitCount(previewTarget);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        previewTarget = null;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        previewTarget = mo;
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        previewTarget = null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;
        // Snapshot before any hit changes the target's HP.
        int hits = hitCount(m);
        for (int i = 0; i < hits; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(-6);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpearcallRitual();
    }
}
