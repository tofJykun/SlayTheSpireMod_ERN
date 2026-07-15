package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class Restage extends CustomCard {
    public static final String ID = "Restage";
    private static final String IMG_PATH = "img/cards/duchess/Restage.png";
    private static final int COST = 1;

    public Restage() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Duchess_COLOR, CardRarity.COMMON, CardTarget.SELF);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int playedCount = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        if (playedCount < 2) {
            return;
        }

        AbstractCard previousCard = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(playedCount - 2);
        AbstractCard copy = previousCard.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(copy);
        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(
                new CardQueueItem(copy, true, EnergyPanel.getCurrentEnergy(), true, true), true);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.actionManager != null
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 1
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Restage();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
