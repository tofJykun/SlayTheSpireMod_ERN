package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.InsightPower;

public class PreceptsStrike extends CustomCard {
    public static final String ID = "PreceptsStrike";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/undertaker/PreceptsStrike.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 8;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int DAMAGE_LOSS_PER_INSIGHT = 3;

    public PreceptsStrike() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot(new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)p, this.damage,
                this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        int insight = getInsightAmount(p);
        if (insight > 0) {
            addToBot((AbstractGameAction)new ModifyDamageAction(this.uuid, -DAMAGE_LOSS_PER_INSIGHT * insight));
        }
    }

    private static int getInsightAmount(AbstractPlayer player) {
        if (player == null || !player.hasPower(InsightPower.POWER_ID)) {
            return 0;
        }
        return player.getPower(InsightPower.POWER_ID).amount;
    }

    @Override
    public AbstractCard makeCopy() {
        return new PreceptsStrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
