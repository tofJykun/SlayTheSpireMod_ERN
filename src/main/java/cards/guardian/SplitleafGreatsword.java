package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;
import powers.HoverPower;

public class SplitleafGreatsword extends CustomCard {
    public static final String ID = "SplitleafGreatsword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/guardian/SplitleafGreatsword.png";
    private static final int COST = -1;
    private static final int DAMAGE = 6;

    public SplitleafGreatsword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = getXValue(p) + (this.upgraded ? 1 : 0);
        this.baseMagicNumber = count;
        this.magicNumber = this.baseMagicNumber;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                    new HoverPower((AbstractCreature)p, count), count));
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    private int getXValue(AbstractPlayer p) {
        int value = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            value = this.energyOnUse;
        }
        if (p != null && p.hasRelic("Chemical X")) {
            value += 2;
        }
        return Math.max(0, value);
    }

    @Override
    public AbstractCard makeCopy() {
        return new SplitleafGreatsword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
