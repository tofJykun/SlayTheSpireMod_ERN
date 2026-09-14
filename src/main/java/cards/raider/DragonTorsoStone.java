package cards.raider;

import basemod.abstracts.CustomCard;
import cards.tempcards.DragonPunch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

import java.util.ArrayList;

public class DragonTorsoStone extends CustomCard {
    public static final String ID = "DragonTorsoStone";
    private static final String IMG_PATH = "img/cards/raider/DragonTorsoStone.png";
    private static final int COST = 1;

    public DragonTorsoStone() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.cardsToPreview = new DragonPunch();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> attacks = new ArrayList<>();
        for (AbstractCard card : p.hand.group) {
            if (card.type == CardType.ATTACK) {
                attacks.add(card);
            }
        }
        if (attacks.isEmpty()) {
            return;
        }

        for (AbstractCard card : attacks) {
            card.unhover();
            card.untip();
            p.hand.removeCard(card);
        }
        p.hand.refreshHandLayout();

        DragonPunch punch = new DragonPunch();
        if (this.upgraded) {
            punch.upgrade();
        }
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(punch, attacks.size()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DragonTorsoStone();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            if (this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
            initializeDescription();
        }
    }
}
