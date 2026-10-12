package actions;

import cards.ironeye.Lush;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.UUID;

public class LushAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final UUID sourceUuid;
    private final boolean removeOriginal;
    private final ArrayList<ShowCardAndObtainEffect> obtains = new ArrayList<>();
    private boolean started;

    public LushAction(AbstractPlayer player, UUID sourceUuid, boolean removeOriginal) {
        this.player = player;
        this.sourceUuid = sourceUuid;
        this.removeOriginal = removeOriginal;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (isDone) return;
        if (!started) {
            started = true;
            if (removeOriginal) {
                for (AbstractCard card : new ArrayList<>(player.masterDeck.group)) {
                    if (Lush.ID.equals(card.cardID) && card.uuid.equals(sourceUuid)) {
                        player.masterDeck.removeCard(card);
                        break;
                    }
                }
            }

            // Snapshot before obtain callbacks can add or modify permanent cards.
            ArrayList<AbstractCard> copies = new ArrayList<>();
            for (AbstractCard card : player.masterDeck.group) {
                AbstractCard copy = card.makeStatEquivalentCopy();
                copy.inBottleFlame = false;
                copy.inBottleLightning = false;
                copy.inBottleTornado = false;
                copies.add(copy);
            }
            for (AbstractCard copy : copies) {
                ShowCardAndObtainEffect effect = new ShowCardAndObtainEffect(
                        copy, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F);
                obtains.add(effect);
                AbstractDungeon.effectList.add(effect);
            }
        }

        // A duplicated play must see the previous play's fully obtained deck.
        for (ShowCardAndObtainEffect effect : obtains) {
            if (!effect.isDone) return;
        }
        isDone = true;
    }
}
