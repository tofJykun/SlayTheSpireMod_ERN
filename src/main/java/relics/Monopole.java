package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Monopole extends CustomRelic {
    public static final String ID = "Monopole";

    public Monopole() {
        super(ID, ImageMaster.loadImage("img/relics/duchess/Monopole.png"),
                ImageMaster.loadImage("img/relics/duchess/outline/Monopole.png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            flash();
            AbstractPlayer player = AbstractDungeon.player;
            addToBot(new RelicAboveCreatureAction(player, this));
            addToBot(new ApplyPowerAction(player, player, new StrengthPower(player, 1), 1));
            addToBot(new ApplyPowerAction(player, player, new LoseStrengthPower(player, 1), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Monopole();
    }
}
