package cards.recluse;

import actions.BedOfMagicDiscardToHandAction;
import actions.BedOfMagicEmberToGlintstoneAction;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import cards.status.MagicEmber;
import cards.tempcards.FadingPrimalGlintstone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.BedOfMagicCostReductionPower;
import powers.BedOfMagicDoublePower;
import powers.BedOfMagicPowerTriggerPower;

import java.util.ArrayList;
import java.util.LinkedList;

public class BedOfMagic extends CustomCard {
    public static final String ID = "BedOfMagic";
    private static final String IMG_PATH = "img/cards/recluse/BedOfMagic.png";
    private static final int COST = 1;
    private static final int EFFECT_COUNT = 3;
    private static final int DAMAGE = 13;
    private static final LinkedList<PlayedType> PLAYED_TYPES = new LinkedList<>();
    private static AbstractCard lastRecordedCard = null;
    private static boolean playedThisTurn = false;
    private final boolean allowSelfPreview;

    public BedOfMagic() {
        this(true);
    }

    private BedOfMagic(boolean allowSelfPreview) {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL);
        this.allowSelfPreview = allowSelfPreview;
        this.selfRetain = true;
        updateDynamicDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Combo combo = getCombo();
        if (combo.hasThreeCards) {
            if (combo.cursesAndStatuses == 3) {
                addToBot((AbstractGameAction)new ExhaustAction(1, false));
            } else if (combo.cursesAndStatuses > 0) {
                addToBot((AbstractGameAction)new DamageAllEnemiesAction((AbstractCreature)p,
                        DamageInfo.createDamageMatrix(DAMAGE, true),
                        DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
                addToBot((AbstractGameAction)new DamageAction((AbstractCreature)p,
                        new DamageInfo((AbstractCreature)p, DAMAGE, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.FIRE));
            } else if (combo.attacks == 3) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new BedOfMagicCostReductionPower((AbstractCreature)p, CardType.SKILL, EFFECT_COUNT), EFFECT_COUNT));
            } else if (combo.attacks == 2 && combo.skills == 1) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new BedOfMagicPowerTriggerPower((AbstractCreature)p, EFFECT_COUNT), EFFECT_COUNT));
            } else if (combo.attacks == 1 && combo.skills == 2) {
                addToBot((AbstractGameAction)new MakeTempCardInHandAction(new FadingPrimalGlintstone(), 1));
            } else if (combo.skills == 3) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new BedOfMagicCostReductionPower((AbstractCreature)p, CardType.ATTACK, EFFECT_COUNT), EFFECT_COUNT));
            } else if (combo.attacks == 2 && combo.powers == 1) {
                addToBot((AbstractGameAction)new BedOfMagicDiscardToHandAction());
            } else if (combo.attacks == 1 && combo.skills == 1 && combo.powers == 1) {
                addToBot((AbstractGameAction)new GainEnergyAction(3));
            } else if (combo.skills == 2 && combo.powers == 1) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new com.megacrit.cardcrawl.powers.EquilibriumPower((AbstractCreature)p, 1), 1));
            } else if (combo.attacks == 1 && combo.powers == 2) {
                addToBot((AbstractGameAction)new BedOfMagicEmberToGlintstoneAction());
            } else if (combo.skills == 1 && combo.powers == 2) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new BedOfMagicDoublePower((AbstractCreature)p, 1), 1));
            } else if (combo.powers == 3) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                        new com.megacrit.cardcrawl.powers.DrawCardNextTurnPower((AbstractCreature)p, 3), 3));
            }
        }
        playedThisTurn = true;
        recordCurrentBedIfMissing(this);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateBedCost();
        updateDynamicDescription();
    }

    @Override
    public void triggerOnGlowCheck() {
        updateDynamicDescription();
        updateBedCost();
        this.glowColor = getCombo().hasThreeCards
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new BedOfMagic();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            updateBedCost();
            updateDynamicDescription();
        }
    }

    public static void recordPlayedCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        lastRecordedCard = card;
        PLAYED_TYPES.add(toPlayedType(card));
        while (PLAYED_TYPES.size() > 3) {
            PLAYED_TYPES.removeFirst();
        }
        refreshAllInstances();
    }

    private static void recordCurrentBedIfMissing(AbstractCard card) {
        if (lastRecordedCard != card) {
            recordPlayedCard(card);
        }
    }

    public static void resetTurn() {
        lastRecordedCard = null;
        playedThisTurn = false;
        refreshAllInstances();
    }

    public static void resetCombat() {
        PLAYED_TYPES.clear();
        lastRecordedCard = null;
        playedThisTurn = false;
        refreshAllInstances();
    }

    private void updateBedCost() {
        if (this.upgraded && !playedThisTurn) {
            setCostForTurn(0);
        } else {
            this.costForTurn = this.cost;
            this.isCostModifiedForTurn = false;
        }
    }

    private void updateDynamicDescription() {
        CardStrings cardStrings = getCardStrings();
        String[] ext = cardStrings.EXTENDED_DESCRIPTION;
        StringBuilder description = new StringBuilder(ext[0]);
        description.append(" NL ");
        description.append(getTypeLine(ext));
        String effectText = getEffectText(ext);
        if (!effectText.isEmpty()) {
            description.append(" NL ");
            description.append(effectText);
        }
        if (this.upgraded) {
            description.append(" NL ");
            description.append(ext[20]);
        }
        this.rawDescription = description.toString();
        initializeDescription();
    }

    private static String getTypeLine(String[] ext) {
        if (PLAYED_TYPES.isEmpty()) {
            return ext[1];
        }
        ArrayList<String> names = new ArrayList<>();
        for (PlayedType type : PLAYED_TYPES) {
            names.add(type.displayName(ext));
        }
        String separator = ext[7];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                builder.append(separator);
            }
            builder.append(names.get(i));
        }
        return builder.toString();
    }

    private String getEffectText(String[] ext) {
        Combo combo = getCombo();
        if (!combo.hasThreeCards) {
            setCardPreviews();
            return "";
        }
        if (combo.cursesAndStatuses == 3) {
            setCardPreviews();
            return ext[19];
        }
        if (combo.cursesAndStatuses > 0) {
            setCardPreviews();
            return ext[18];
        }
        if (combo.attacks == 3) {
            setCardPreviews();
            return ext[8];
        }
        if (combo.attacks == 2 && combo.skills == 1) {
            setCardPreviews(new MagicEmber());
            return ext[9];
        }
        if (combo.attacks == 1 && combo.skills == 2) {
            setCardPreviews(new FadingPrimalGlintstone());
            return ext[10];
        }
        if (combo.skills == 3) {
            setCardPreviews();
            return ext[11];
        }
        if (combo.attacks == 2 && combo.powers == 1) {
            setCardPreviews();
            return ext[12];
        }
        if (combo.attacks == 1 && combo.skills == 1 && combo.powers == 1) {
            setCardPreviews();
            return ext[13];
        }
        if (combo.skills == 2 && combo.powers == 1) {
            setCardPreviews();
            return ext[14];
        }
        if (combo.attacks == 1 && combo.powers == 2) {
            setCardPreviews(new MagicEmber(), new FadingPrimalGlintstone());
            return ext[15];
        }
        if (combo.skills == 1 && combo.powers == 2) {
            if (this.allowSelfPreview) {
                setCardPreviews(new BedOfMagic(false));
            } else {
                setCardPreviews();
            }
            return ext[16];
        }
        if (combo.powers == 3) {
            setCardPreviews();
            return ext[17];
        }
        setCardPreviews();
        return "";
    }

    private void setCardPreviews(AbstractCard... previews) {
        this.cardsToPreview = null;
        MultiCardPreview.clear(this);
        if (previews.length == 1) {
            this.cardsToPreview = previews[0];
        } else if (previews.length > 1) {
            MultiCardPreview.add(this, true, previews);
        }
    }

    private static Combo getCombo() {
        Combo combo = new Combo();
        combo.hasThreeCards = PLAYED_TYPES.size() == 3;
        for (PlayedType type : PLAYED_TYPES) {
            switch (type) {
                case ATTACK:
                    combo.attacks++;
                    break;
                case SKILL:
                    combo.skills++;
                    break;
                case POWER:
                    combo.powers++;
                    break;
                case STATUS:
                case CURSE:
                    combo.cursesAndStatuses++;
                    break;
                default:
                    break;
            }
        }
        return combo;
    }

    private static PlayedType toPlayedType(AbstractCard card) {
        if (card.type == CardType.ATTACK) {
            return PlayedType.ATTACK;
        }
        if (card.type == CardType.SKILL) {
            return PlayedType.SKILL;
        }
        if (card.type == CardType.POWER) {
            return PlayedType.POWER;
        }
        if (card.type == CardType.CURSE) {
            return PlayedType.CURSE;
        }
        return PlayedType.STATUS;
    }

    private static void refreshAllInstances() {
        if (AbstractDungeon.player == null) {
            return;
        }
        refreshGroup(AbstractDungeon.player.hand);
        refreshGroup(AbstractDungeon.player.drawPile);
        refreshGroup(AbstractDungeon.player.discardPile);
        refreshGroup(AbstractDungeon.player.exhaustPile);
    }

    private static void refreshGroup(com.megacrit.cardcrawl.cards.CardGroup group) {
        for (AbstractCard card : group.group) {
            if (card instanceof BedOfMagic) {
                ((BedOfMagic)card).updateBedCost();
                ((BedOfMagic)card).updateDynamicDescription();
            }
        }
    }

    private enum PlayedType {
        ATTACK,
        SKILL,
        POWER,
        STATUS,
        CURSE;

        private String displayName(String[] ext) {
            if (this == ATTACK) {
                return ext[2];
            }
            if (this == SKILL) {
                return ext[3];
            }
            if (this == POWER) {
                return ext[4];
            }
            if (this == STATUS) {
                return ext[5];
            }
            return ext[6];
        }
    }

    private static class Combo {
        private boolean hasThreeCards;
        private int attacks;
        private int skills;
        private int powers;
        private int cursesAndStatuses;
    }
}
