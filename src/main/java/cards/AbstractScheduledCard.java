package cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import patches.ScheduledField;

import java.util.Collections;
import java.util.List;

public abstract class AbstractScheduledCard extends CustomCard {
    protected AbstractScheduledCard(String id, String name, String img, int cost, String rawDescription,
                                    AbstractCard.CardType type, AbstractCard.CardColor color,
                                    AbstractCard.CardRarity rarity, AbstractCard.CardTarget target,
                                    int defaultScheduled) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        ScheduledField.setBaseScheduled(this, defaultScheduled);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        int current = ScheduledField.getScheduled(this);
        int base = ScheduledField.getBaseScheduled(this);
        if (Settings.language == Settings.GameLanguage.ZHS) {
            return Collections.singletonList(new TooltipInfo("定时",
                    "如果你在这回合打出的牌数少于999张，这张牌将在打出 #b" + current
                            + " 张牌后自动打出。在这张牌进入或离开手牌时重置到 #b" + base + " 。"));
        }
        return Collections.singletonList(new TooltipInfo("Scheduled",
                "If you have played fewer than 999 cards this turn, this card is automatically played after you play #b"
                        + current + " cards. Reset to #b" + base + " whenever this card enters or leaves your hand."));
    }

    public void onScheduledCountChanged(int current, int base) {
    }
}
