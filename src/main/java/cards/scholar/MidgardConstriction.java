package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import relics.SunStone;

public class MidgardConstriction extends CustomCard {
    public static final String ID = "MidgardConstriction";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public MidgardConstriction() {
        super(ID, STRINGS.NAME, "img/cards/scholar/MidgardConstriction.png", 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 50;
        isMultiDamage = true;
    }

    private static boolean meetsCondition(AbstractPlayer player) {
        if (player == null) return false;
        if (player.hasRelic(SunStone.ID)) return true;
        for (AbstractCard card : player.masterDeck.group) {
            String id = card.cardID;
            if (Daat.ID.equals(id) || LordOfLords.ID.equals(id) || Alkahest.ID.equals(id)
                    || BrandNewWorld.ID.equals(id) || Emerge.ID.equals(id) || ParadiseLost.ID.equals(id)
                    || Empyrean.ID.equals(id) || TreeOfLife.ID.equals(id)) return true;
        }
        return false;
    }

    @Override public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (p == null || !super.canUse(p, m)) return false;
        if (!meetsCondition(p)) {
            cantUseMessage = STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override public void triggerOnGlowCheck() {
        glowColor = meetsCondition(AbstractDungeon.player)
                ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    @Override public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeDamage(15); }
    }

    @Override public AbstractCard makeCopy() { return new MidgardConstriction(); }
}
