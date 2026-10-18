package cards.scholar;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import patches.AbstractCardEnum;

public class Prakriti extends CustomCard {
    public static final String ID = "Prakriti";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private int lastBonus = -1;
    private boolean lastInCombat;

    public Prakriti() {
        super(ID, STRINGS.NAME, "img/cards/scholar/Prakriti.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseBlock = block = 4;
        baseMagicNumber = magicNumber = 2;
    }

    private boolean inCombat() {
        return AbstractDungeon.player != null && CombatState.isInCombat();
    }

    private int pileBonus() {
        if (!inCombat()) return 0;
        AbstractPlayer p = AbstractDungeon.player;
        int count = 0;
        for (CardGroup pile : new CardGroup[] {p.drawPile, p.discardPile, p.hand}) {
            if (pile != null) {
                for (AbstractCard card : pile.group) {
                    if (ID.equals(card.cardID)) count++;
                }
            }
        }
        return count * magicNumber;
    }

    private void calculate(AbstractMonster monster, boolean targeted) {
        int originalDamage = baseDamage;
        int originalBlock = baseBlock;
        lastInCombat = inCombat();
        lastBonus = pileBonus();
        baseDamage += lastBonus;
        baseBlock += lastBonus;
        try {
            if (!lastInCombat) {
                damage = baseDamage;
                block = baseBlock;
            } else if (targeted) {
                super.calculateCardDamage(monster);
            } else {
                super.applyPowers();
            }
        } finally {
            // The pile bonus must not become permanent or be counted again on copies/refreshes.
            baseDamage = originalDamage;
            baseBlock = originalBlock;
        }
        isDamageModified = damage != baseDamage;
        isBlockModified = block != baseBlock;
    }

    @Override
    public void applyPowers() { calculate(null, false); }

    @Override
    public void calculateCardDamage(AbstractMonster monster) { calculate(monster, true); }

    @Override
    public void update() {
        super.update();
        if (lastBonus != pileBonus() || lastInCombat != inCombat()) applyPowers();
    }

    @Override
    public void render(SpriteBatch sb, boolean selected) {
        if (!inCombat() || !AbstractDungeon.player.hand.contains(this)) applyPowers();
        super.render(sb, selected);
    }

    @Override
    public void onMoveToDiscard() { applyPowers(); }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(1);
            upgradeBlock(1);
            upgradeMagicNumber(1);
            applyPowers();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.applyPowers();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() { return new Prakriti(); }
}
