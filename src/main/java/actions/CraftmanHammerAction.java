package actions;

import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import general.SmithingHelper;
import patches.InsuranceField;
import powers.MasterworkPower;

public class CraftmanHammerAction extends AbstractGameAction {
    private static final String SELECT_MATERIAL_ZH = "选择1张锻造素材。";
    private static final String SELECT_BODY_ZH = "选择1张非锻造素材牌。";
    private static final String SELECT_MATERIAL_ENG = "Choose 1 smithing material.";
    private static final String SELECT_BODY_ENG = "Choose 1 non-material card.";

    private final AbstractPlayer player;
    private AbstractCard selectedMaterial;
    private int step = 0;

    public CraftmanHammerAction() {
        this.player = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.player == null || !hasMaterialAndBody()) {
            this.isDone = true;
            return;
        }

        if (this.step == 0) {
            openMaterialSelection();
            return;
        }

        if (this.step == 1) {
            saveMaterialSelection();
            if (this.selectedMaterial == null) {
                this.isDone = true;
                return;
            }
            openBodySelection();
            return;
        }

        if (this.step == 2) {
            saveBodySelectionAndForge();
            this.isDone = true;
        }
    }

    private boolean hasMaterialAndBody() {
        boolean hasMaterial = false;
        boolean hasBody = false;
        for (AbstractCard card : this.player.hand.group) {
            if (SmithingHelper.isSmithingMaterial(card)) {
                hasMaterial = true;
            } else {
                hasBody = true;
            }
        }
        return hasMaterial && hasBody;
    }

    private void openMaterialSelection() {
        CardGroup materials = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : this.player.hand.group) {
            if (SmithingHelper.isSmithingMaterial(card)) {
                materials.addToTop(card);
            }
        }
        AbstractDungeon.gridSelectScreen.open(materials, 1, true,
                Settings.language == Settings.GameLanguage.ZHS ? SELECT_MATERIAL_ZH : SELECT_MATERIAL_ENG);
        this.step = 1;
    }

    private void saveMaterialSelection() {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.selectedMaterial = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    private void openBodySelection() {
        CardGroup bodies = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : this.player.hand.group) {
            if (!SmithingHelper.isSmithingMaterial(card) && card != this.selectedMaterial) {
                bodies.addToTop(card);
            }
        }
        if (bodies.isEmpty()) {
            this.isDone = true;
            return;
        }
        AbstractDungeon.gridSelectScreen.open(bodies, 1,
                Settings.language == Settings.GameLanguage.ZHS ? SELECT_BODY_ZH : SELECT_BODY_ENG,
                false, false, false, false);
        this.step = 2;
    }

    private void saveBodySelectionAndForge() {
        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            return;
        }

        AbstractCard selectedBody = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        if (this.selectedMaterial == null || selectedBody == null
                || !this.player.hand.group.contains(this.selectedMaterial)
                || !this.player.hand.group.contains(selectedBody)) {
            return;
        }

        CraftmanCreation creation = createForgedCard(this.selectedMaterial, selectedBody);
        this.player.hand.removeCard(this.selectedMaterial);
        this.player.hand.removeCard(selectedBody);
        addToTop(new MakeTempCardInHandAction(creation, 1));
        MasterworkPower.onSmithing(this.player);
        this.player.hand.refreshHandLayout();
    }

    private CraftmanCreation createForgedCard(AbstractCard material, AbstractCard selectedBody) {
        AbstractCard infusion = null;
        AbstractCard body = selectedBody;
        AbstractCard reinforcement = null;

        if (selectedBody instanceof CraftmanCreation) {
            CraftmanCreation creation = (CraftmanCreation)selectedBody;
            infusion = creation.getInfusionMaterialCopy();
            body = creation.getBodyCardCopy();
            reinforcement = creation.getReinforcementMaterialCopy();
        }

        if (SmithingHelper.isInfusionMaterial(material)) {
            infusion = material;
        } else if (SmithingHelper.isReinforcementMaterial(material)) {
            reinforcement = material;
        }

        CraftmanCreation result = new CraftmanCreation(infusion, body, reinforcement);
        InsuranceField.inherit(result, selectedBody);
        return result;
    }
}
