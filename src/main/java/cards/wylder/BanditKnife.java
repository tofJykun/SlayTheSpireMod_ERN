package cards.wylder;

import actions.GainGoldWithAnimationAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.StunPower;

public class BanditKnife extends CustomCard {
    public static final String ID = "BanditKnife";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public BanditKnife() {
        super(ID, STRINGS.NAME, "img/cards/wylder/BanditKnife.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Wylder_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 13;
        baseMagicNumber = magicNumber = 25;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) return;
        final boolean stunned = m.hasPower(StunPower.POWER_ID) || m.intent == AbstractMonster.Intent.STUN;
        final int gold = magicNumber;
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY) {
            @Override
            public void update() {
                if (isDone) return;
                boolean cancelled = shouldCancelAction() || p.isDying || p.halfDead;
                super.update();
                // Pay within the hit action, even when a lethal hit clears the queue.
                if (isDone && !cancelled && stunned) {
                    new GainGoldWithAnimationAction(gold).update();
                }
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new BanditKnife(); }
}
