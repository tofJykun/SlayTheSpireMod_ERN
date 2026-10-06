package relics;

import basemod.abstracts.CustomRelic;
import cards.tempcards.AbstractPhantomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.JellyfishShieldPower;

public class WhetstoneKnife extends CustomRelic {
    public static final String ID = "WhetstoneKnife";
    private static final int DAMAGE_BONUS = 3;

    public WhetstoneKnife() {
        super(ID, ImageMaster.loadImage("img/relics/revenant/WhetstoneKnife.png"),
                ImageMaster.loadImage("img/relics/revenant/outline/WhetstoneKnife.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    private static boolean isSlashAttack(AbstractCard card) {
        return card instanceof AbstractPhantomCard && card.type == AbstractCard.CardType.ATTACK;
    }

    @Override
    public float atDamageModify(float damage, AbstractCard card) {
        return isSlashAttack(card) ? damage + DAMAGE_BONUS : damage;
    }

    public static void refreshPhantomDamage(AbstractCard card) {
        if (!isSlashAttack(card)) return;
        // Existing Phantom attacks use Spirit-adjusted magicNumber, also shared by non-damage effects.
        card.baseDamage = card.baseMagicNumber;
        card.damage = card.magicNumber;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            card.damage += DAMAGE_BONUS;
        }
        // Phantom attacks replace vanilla damage, so apply this final multiplier after their flat bonuses.
        AbstractPower jellyfish = AbstractDungeon.player == null ? null
                : AbstractDungeon.player.getPower(JellyfishShieldPower.POWER_ID);
        if (jellyfish != null) {
            card.damage = Math.max(0, (int)Math.floor(
                    jellyfish.atDamageFinalGive(card.damage, card.damageTypeForTurn)));
        }
        card.isDamageModified = card.damage != card.baseDamage;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WhetstoneKnife();
    }
}
