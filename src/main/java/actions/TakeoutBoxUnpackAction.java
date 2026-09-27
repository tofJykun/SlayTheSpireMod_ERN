package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import general.PackagingCard;

import java.util.ArrayList;

public class TakeoutBoxUnpackAction extends AbstractGameAction {
    private final PackagingCard box;
    private final boolean clearBeforeContainerCleanup;
    private final ArrayList<AbstractCard> originals;

    public TakeoutBoxUnpackAction(AbstractCard boxCard, PackagingCard box) {
        this(boxCard, box, true);
    }

    public TakeoutBoxUnpackAction(AbstractCard boxCard, PackagingCard box, boolean clearBeforeContainerCleanup) {
        this.box = box;
        this.clearBeforeContainerCleanup = clearBeforeContainerCleanup;
        // Capture at use(), before the container's cleanup can clear its contents.
        this.originals = box == null ? new ArrayList<>() : new ArrayList<>(box.getPackagedCards());
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.isDone) return;
        this.isDone = true;
        if (this.originals.isEmpty()) return;
        if (this.clearBeforeContainerCleanup) this.box.clearPackagedCards();
        PackagingSequencer.request(this.originals);
    }
}
