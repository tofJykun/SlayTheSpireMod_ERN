package cards.duchess;

import actions.EuporiaAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class Euporia extends CustomCard {
    public static final String ID = "Euporia";
    private static final String IMG_PATH = "img/cards/duchess/Euporia.png";
    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int BASE_SEGMENTS = 5;
    private static final int ALL_ENEMIES_THRESHOLD = 10;
    private static final int SEGMENTS_PER_KILL = 1;
    private static final int UPGRADE_PLUS_DAMAGE = 1;

    private int damageSegments = BASE_SEGMENTS;

    public Euporia() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE,
                CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = BASE_SEGMENTS;
        this.magicNumber = this.baseMagicNumber;
        updateDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getDamageSegments() {
        return this.damageSegments;
    }

    public boolean hitsAllEnemies() {
        return this.damageSegments >= ALL_ENEMIES_THRESHOLD;
    }

    public void registerKill() {
        this.damageSegments += SEGMENTS_PER_KILL;
        syncInBattleCopies();
    }

    private void syncInBattleCopies() {
        for (AbstractCard card : GetAllInBattleInstances.get(this.uuid)) {
            if (card instanceof Euporia && card != this) {
                Euporia copy = (Euporia)card;
                copy.damageSegments = this.damageSegments;
                copy.updateDescription();
            }
        }
        this.baseMagicNumber = this.damageSegments;
        this.magicNumber = this.damageSegments;
        updateDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EuporiaAction(p, this, this.damageSegments, this.hitsAllEnemies()));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        updateDescription();
    }

    private void updateDescription() {
        CardStrings cardStrings = getCardStrings();
        String description = cardStrings.DESCRIPTION
                .replace("!MD!", Integer.toString(ALL_ENEMIES_THRESHOLD))
                .replace("!D!", Integer.toString(this.damage))
                .replace("!M!", Integer.toString(this.damageSegments))
                .replace("!MS!", Integer.toString(SEGMENTS_PER_KILL));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Euporia copy = (Euporia)super.makeStatEquivalentCopy();
        copy.damageSegments = this.damageSegments;
        copy.baseMagicNumber = this.damageSegments;
        copy.magicNumber = this.damageSegments;
        copy.updateDescription();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Euporia();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            updateDescription();
        }
    }
}
