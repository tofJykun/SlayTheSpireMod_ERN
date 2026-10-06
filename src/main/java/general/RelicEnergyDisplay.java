package general;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.HashMap;
import java.util.Map;

/** Relic previews have no player, so their energy symbols need an explicit card color. */
public final class RelicEnergyDisplay {
    private static final Map<String, String> tokens = new HashMap<>();
    private static final Map<String, AbstractCard.CardColor> colors = new HashMap<>();

    private RelicEnergyDisplay() {}

    public static void register(String relicId, AbstractCard.CardColor color) {
        String token = "[ERN_ENERGY_" + color.name() + "]";
        tokens.put(relicId, token);
        colors.put(token, color);
    }

    public static void prepare(AbstractRelic relic) {
        if (relic == null) return;
        String token = tokens.get(relic.relicId);
        if (token == null) return;
        if (relic.description != null) relic.description = relic.description.replace("[E]", token);
        for (PowerTip tip : relic.tips) {
            if (tip.body != null) tip.body = tip.body.replace("[E]", token);
        }
    }

    public static TextureAtlas.AtlasRegion orb(String token) {
        AbstractCard.CardColor color = colors.get(token);
        return color == null ? null : BaseMod.getCardEnergyOrbAtlasRegion(color);
    }
}
