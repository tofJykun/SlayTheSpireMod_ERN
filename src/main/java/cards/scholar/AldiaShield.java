package cards.scholar;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import java.util.HashSet;
import java.util.Set;
import patches.AbstractCardEnum;

public class AldiaShield extends CustomCard {
    public static final String ID = "AldiaShield";
    private int lastBonus = -1;
    private boolean lastInCombat;

    public AldiaShield() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/AldiaShield.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 8;
        baseMagicNumber = magicNumber = 1;
    }

    private boolean inCombat() {
        return AbstractDungeon.player != null && CombatState.isInCombat();
    }

    private int deckBonus() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) return 0;
        Set<String> ids = new HashSet<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) ids.add(card.cardID);
        return ids.size() * magicNumber;
    }

    private void calculate(AbstractMonster monster, boolean targeted) {
        int originalBlock = baseBlock;
        lastBonus = deckBonus();
        lastInCombat = inCombat();
        baseBlock += lastBonus;
        try {
            if (!lastInCombat) block = baseBlock;
            else if (targeted) super.calculateCardDamage(monster);
            else super.applyPowers();
        } finally {
            // Keep deck diversity separate from permanent card modifications and copied base values.
            baseBlock = originalBlock;
        }
        isBlockModified = block != baseBlock;
    }

    @Override public void applyPowers() { calculate(null, false); }

    @Override public void calculateCardDamage(AbstractMonster monster) { calculate(monster, true); }

    @Override public void update() {
        super.update();
        if (lastBonus != deckBonus() || lastInCombat != inCombat()) applyPowers();
    }

    @Override public void render(SpriteBatch sb, boolean selected) {
        if (!inCombat() || !AbstractDungeon.player.hand.contains(this)) applyPowers();
        super.render(sb, selected);
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            upgradedBlock = true;
            applyPowers();
        }
    }

    @Override public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.applyPowers();
        return copy;
    }

    @Override public AbstractCard makeCopy() { return new AldiaShield(); }
}
