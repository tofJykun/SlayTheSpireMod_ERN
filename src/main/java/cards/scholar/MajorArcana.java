package cards.scholar;

import actions.MajorArcanaAction;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CombatState;
import general.MajorArcanaCycle;
import patches.AbstractCardEnum;

public class MajorArcana extends CustomCard {
    public static final String ID = "MajorArcana";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private long combat = -1;
    private int lockedStage;
    private int displayedStage;
    private boolean displayedUpgrade;

    public MajorArcana() {
        super(ID, STRINGS.NAME, "img/cards/scholar/MajorArcana.png", 1, STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        refresh();
    }

    public int stage() {
        if (AbstractDungeon.player == null || !CombatState.isInCombat()) return 1;
        if (combat != MajorArcanaCycle.combat()) {
            combat = MajorArcanaCycle.combat();
            lockedStage = 0;
        }
        return lockedStage > 0 ? lockedStage : MajorArcanaCycle.nextStage();
    }

    public void refresh() {
        int stage = stage();
        if (displayedStage == stage && displayedUpgrade == upgraded) return;
        displayedStage = stage;
        displayedUpgrade = upgraded;
        baseBlock = block = stage == 1 ? 10 : 0;
        int[] values = {0, 4, 4, 0, 2, 0, 3, 2, 2, 2, 0, 0, 2, 10, 50, 0, 50, 2, 3, 1, 0, 0};
        baseMagicNumber = magicNumber = values[stage - 1];
        target = stage == 3 || stage == 14 ? CardTarget.ENEMY
                : stage == 19 || stage == 21 ? CardTarget.ALL_ENEMY : CardTarget.SELF;
        rawDescription = STRINGS.EXTENDED_DESCRIPTION[(upgraded ? 22 : 0) + stage - 1];
        initializeDescription();
    }

    @Override public void initializeDescription() {
        super.initializeDescription();
        // Native parsing marks keywords with '*'. The tarot heading is plain prose.
        if (description != null && !description.isEmpty()) {
            DescriptionLine line = description.get(0);
            DescriptionLine plain = new DescriptionLine(line.text.replace("*", ""), line.width);
            plain.width = line.width;
            description.set(0, plain);
        }
        if (displayedStage == 21) {
            keywords.remove("审判");
            keywords.remove("judgment");
            keywords.remove("judgement");
        }
    }

    @Override public void update() { refresh(); super.update(); }
    @Override public void render(SpriteBatch sb, boolean selected) { refresh(); super.render(sb, selected); }
    private void calculate(AbstractMonster monster, boolean targeted) {
        refresh();
        // Compendium previews have no player; StSLib's combat hooks require one.
        if (AbstractDungeon.player == null || !CombatState.isInCombat()) {
            block = baseBlock;
            damage = baseDamage;
            magicNumber = baseMagicNumber;
            isBlockModified = isDamageModified = isMagicNumberModified = false;
        } else if (targeted) {
            super.calculateCardDamage(monster);
        } else {
            super.applyPowers();
        }
    }

    @Override public void applyPowers() { calculate(null, false); }
    @Override public void calculateCardDamage(AbstractMonster m) { calculate(m, true); }
    @Override public boolean canUse(AbstractPlayer p, AbstractMonster m) { refresh(); return super.canUse(p, m); }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        refresh();
        lockedStage = stage();
        combat = MajorArcanaCycle.combat();
        addToBot(new MajorArcanaAction(p, m, lockedStage, block, magicNumber));
    }

    @Override public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            refresh();
        }
    }

    @Override public AbstractCard makeStatEquivalentCopy() {
        MajorArcana copy = (MajorArcana)super.makeStatEquivalentCopy();
        copy.combat = combat;
        copy.lockedStage = lockedStage;
        copy.displayedStage = 0;
        copy.refresh();
        return copy;
    }

    @Override public AbstractCard makeCopy() { return new MajorArcana(); }
}
