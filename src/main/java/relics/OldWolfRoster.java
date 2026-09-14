package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class OldWolfRoster extends CustomRelic {
    public static final String ID = "OldWolfRoster";
    private static final String IMG = "img/relics/wylder/OldWolfRoster.png";
    private static final String IMG_OTL = "img/relics/wylder/outline/OldWolfRoster.png";
    private static final int REQUIRED_UNCOMMON_ATTACKS = 3;
    private static final int STRENGTH_GAIN = 3;

    public OldWolfRoster() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_OTL),
                RelicTier.COMMON, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return;
        }
        int uncommonAttacks = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.rarity == AbstractCard.CardRarity.UNCOMMON && card.type == AbstractCard.CardType.ATTACK) {
                uncommonAttacks++;
            }
        }
        if (uncommonAttacks >= REQUIRED_UNCOMMON_ATTACKS) {
            flash();
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RelicAboveCreatureAction(
                    (AbstractCreature)AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction(
                    (AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player,
                    new StrengthPower((AbstractCreature)AbstractDungeon.player, STRENGTH_GAIN), STRENGTH_GAIN));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OldWolfRoster();
    }
}
