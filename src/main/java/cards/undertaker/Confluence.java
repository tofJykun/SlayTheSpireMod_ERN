package cards.undertaker;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.ConfluencePower;

public class Confluence extends CustomCard {
    public static final String ID = "Confluence";
    private static final String IMG_PATH = "img/cards/undertaker/Confluence.png";
    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int MIN_COST = 2;
    private static final int EXTRA_DAMAGE = 3;
    private static final int UPGRADE_PLUS_EXTRA_DAMAGE = 2;

    public Confluence() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Undertaker_COLOR, CardRarity.UNCOMMON,
                CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = MIN_COST;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void applyPowers() {
        setDamageFromConfluence();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setDamageFromConfluence();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        if (m != null) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new ConfluencePower((AbstractCreature)p), 1));
    }

    @Override
    public void onMoveToDiscard() {
        this.baseDamage = DAMAGE;
    }

    private void setDamageFromConfluence() {
        int count = 0;
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(ConfluencePower.POWER_ID)) {
            count = AbstractDungeon.player.getPower(ConfluencePower.POWER_ID).amount;
        }
        int extra = this.upgraded ? EXTRA_DAMAGE + UPGRADE_PLUS_EXTRA_DAMAGE : EXTRA_DAMAGE;
        this.baseDamage = DAMAGE + Math.max(0, count) * extra;
    }

    private void refreshDescription() {
        String description = getCardStrings().DESCRIPTION
                .replace("!M!", Integer.toString(this.magicNumber))
                .replace("!MD!", Integer.toString(this.upgraded
                        ? EXTRA_DAMAGE + UPGRADE_PLUS_EXTRA_DAMAGE : EXTRA_DAMAGE));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Confluence();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            refreshDescription();
        }
    }
}
