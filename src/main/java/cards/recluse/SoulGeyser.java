package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class SoulGeyser extends CustomCard {
    public static final String ID = "SoulGeyser";
    private static final String IMG_PATH = "img/cards/recluse/SoulGeyser.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int HIT_COUNT = 2;
    private static final int DRAW = 3;
    private static final int SKILL_COUNT = 3;

    public SoulGeyser() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                    new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        if (lastThreeCardsAreSkills()) {
            addToBot((AbstractGameAction)new DrawCardAction((AbstractCreature)p, DRAW));
        }
    }

    private boolean lastThreeCardsAreSkills() {
        if (AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisCombat.size() < SKILL_COUNT) {
            return false;
        }

        int end = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.get(end - 1) == this) {
            end--;
        }
        if (end < SKILL_COUNT) {
            return false;
        }

        for (int i = end - SKILL_COUNT; i < end; i++) {
            if (AbstractDungeon.actionManager.cardsPlayedThisCombat.get(i).type != CardType.SKILL) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = lastThreeCardsAreSkills()
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new SoulGeyser();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
