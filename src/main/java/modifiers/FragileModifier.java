package modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import patches.FragileField;
import patches.DarkswordDescriptionPatch;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FragileModifier extends AbstractCardModifier {
    public static final String ID = "ERN:Fragile";
    private static final Pattern COUNT = Pattern.compile("(易损|Fragile)(\\s+)(?:[0-9]+|![A-Za-z][A-Za-z0-9]*!)");

    @Override public String identifier(AbstractCard card) { return ID; }
    @Override public boolean isInherent(AbstractCard card) { return true; }
    @Override public boolean shouldApply(AbstractCard card) { return !CardModifierManager.hasModifier(card, ID); }
    @Override public AbstractCardModifier makeCopy() { return new FragileModifier(); }

    @Override
    public String modifyDescription(String raw, AbstractCard card) {
        if (raw == null || !FragileField.isFragile(card) || DarkswordDescriptionPatch.shouldHideDescription(card)) return raw;
        if (COUNT.matcher(raw).find()) return COUNT.matcher(raw).replaceAll("$1$2!ERNFragile!");
        return raw + CardCrawlGame.languagePack.getUIString("Fragile").TEXT[4];
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        if (!FragileField.isFragile(card) || DarkswordDescriptionPatch.shouldHideDescription(card)) return null;
        String[] text = CardCrawlGame.languagePack.getUIString("Fragile").TEXT;
        long count = (long)FragileField.value(card) + 1;
        return Collections.singletonList(new TooltipInfo(text[0],
                text[FragileField.usesCombatCount(card) ? 2 : 1] + count
                        + text[FragileField.usesCombatCount(card) ? 3 : 5]));
    }
}
