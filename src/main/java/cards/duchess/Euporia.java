package cards.duchess;

import actions.EuporiaAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

import java.util.HashSet;
import java.util.Set;

public class Euporia extends CustomCard {
    public static final String ID = "Euporia";
    private static final String IMG_PATH = "img/cards/duchess/Euporia.png";
    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int BASE_SEGMENTS = 5;
    private static final int ALL_ENEMIES_THRESHOLD = 10;
    private static final int SEGMENTS_PER_KILL = 1;
    private static final int UPGRADE_PLUS_DAMAGE = 1;

    public Euporia() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE,
                CardTarget.ALL_ENEMY);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = BASE_SEGMENTS;
        this.magicNumber = this.baseMagicNumber;
        updateDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public int getDamageSegments() {
        return (int)Math.min(Integer.MAX_VALUE, (long)BASE_SEGMENTS + Math.max(0, this.misc));
    }

    public boolean hitsAllEnemies() {
        return getDamageSegments() >= ALL_ENEMIES_THRESHOLD;
    }

    public void registerKill() {
        // misc is saved by the game; UUID ties combat instances to their deck entry.
        Set<AbstractCard> instances = new HashSet<>();
        instances.add(this);
        if (AbstractDungeon.player != null) {
            instances.addAll(GetAllInBattleInstances.get(this.uuid));
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (this.uuid.equals(card.uuid)) instances.add(card);
            }
        }
        for (AbstractCard card : instances) {
            if (card instanceof Euporia) {
                card.misc = (int)Math.min(Integer.MAX_VALUE - BASE_SEGMENTS,
                        (long)Math.max(0, card.misc) + SEGMENTS_PER_KILL);
                ((Euporia)card).updateDescription();
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EuporiaAction(p, this, getDamageSegments(), this.hitsAllEnemies()));
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
        this.baseMagicNumber = this.magicNumber = getDamageSegments();
        this.isMagicNumberModified = false;
        CardStrings cardStrings = getCardStrings();
        String description = cardStrings.DESCRIPTION
                .replace("!MD!", Integer.toString(ALL_ENEMIES_THRESHOLD))
                .replace("!MS!", Integer.toString(SEGMENTS_PER_KILL));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Euporia copy = (Euporia)super.makeStatEquivalentCopy();
        copy.updateDescription();
        return copy;
    }

    @Override
    public void onLoadedMisc() {
        updateDescription();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        updateDescription();
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
