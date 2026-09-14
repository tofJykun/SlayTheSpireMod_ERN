package cards.ironeye;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.PotionSlot;
import patches.AbstractCardEnum;

public class VendingMachine extends CustomCard {
    public static final String ID = "VendingMachine";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "img/cards/ironeye/VendingMachine.png";
    private static final int COST = 1;
    private static final int GOLD_LOSS = 40;
    private static final int UPGRADE_GOLD_LOSS = 30;

    public VendingMachine() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Ironeye_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = GOLD_LOSS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseGold(this.magicNumber);
        int emptySlots = countEmptyPotionSlots(p);
        for (int i = 0; i < emptySlots; i++) {
            addToBot((AbstractGameAction)new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        }
    }

    private static int countEmptyPotionSlots(AbstractPlayer player) {
        int count = 0;
        if (player == null) {
            return count;
        }
        int checkedSlots = Math.min(player.potionSlots, player.potions.size());
        for (int i = 0; i < checkedSlots; i++) {
            if (player.potions.get(i) instanceof PotionSlot) {
                count++;
            }
        }
        return count;
    }

    @Override
    public AbstractCard makeCopy() {
        return new VendingMachine();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.baseMagicNumber = UPGRADE_GOLD_LOSS;
            this.magicNumber = this.baseMagicNumber;
            initializeDescription();
        }
    }
}
