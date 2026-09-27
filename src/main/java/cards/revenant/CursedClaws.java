package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class CursedClaws extends CustomCard {
    public static final String ID = "CursedClaws";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/revenant/CursedClaws.png";
    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DAMAGE_GAIN = 5;
    private static final int UPGRADE_PLUS_DAMAGE_GAIN = 2;

    public CursedClaws() {
        this(true);
    }

    private CursedClaws(boolean addPreview) {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = DAMAGE_GAIN;
        this.magicNumber = this.baseMagicNumber;
        if (addPreview) {
            this.cardsToPreview = new CursedClaws(false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot((AbstractGameAction)new IncreaseAllCursedClawsDamageAction(this, this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (EnergyPanel.totalCount != 0) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = EnergyPanel.totalCount == 0
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new CursedClaws();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_GAIN);
            if (this.cardsToPreview != null && !this.cardsToPreview.upgraded) {
                this.cardsToPreview.upgrade();
            }
        }
    }

    private static class IncreaseAllCursedClawsDamageAction extends AbstractGameAction {
        private final AbstractCard source;
        private final int amount;

        private IncreaseAllCursedClawsDamageAction(AbstractCard source, int amount) {
            this.source = source;
            this.amount = amount;
        }

        @Override
        public void update() {
            Set<AbstractCard> cards = collectCombatCards(this.source);
            for (AbstractCard card : cards) {
                if (ID.equals(card.cardID)) {
                    card.baseDamage += this.amount;
                    card.applyPowers();
                }
            }
            this.isDone = true;
        }

        private static Set<AbstractCard> collectCombatCards(AbstractCard source) {
            Set<AbstractCard> cards = Collections.newSetFromMap(new IdentityHashMap<AbstractCard, Boolean>());
            if (source != null) {
                cards.add(source);
            }
            if (AbstractDungeon.player == null) {
                return cards;
            }

            addGroup(cards, AbstractDungeon.player.drawPile);
            addGroup(cards, AbstractDungeon.player.hand);
            addGroup(cards, AbstractDungeon.player.discardPile);
            addGroup(cards, AbstractDungeon.player.exhaustPile);
            addGroup(cards, AbstractDungeon.player.limbo);

            if (AbstractDungeon.actionManager != null) {
                for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
                    if (item != null && item.card != null) {
                        cards.add(general.SmithingBody.behavior(item.card));
                    }
                }
            }
            return cards;
        }

        private static void addGroup(Set<AbstractCard> cards, CardGroup group) {
            if (group != null) {
                for (AbstractCard card : group.group) cards.add(general.SmithingBody.behavior(card));
            }
        }
    }
}
