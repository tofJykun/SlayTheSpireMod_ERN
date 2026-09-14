package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.PowerOfVengeancePower;

import java.util.UUID;

public class PowerOfVengeance extends CustomCard {
    public static final String ID = "PowerOfVengeance";
    private static final String IMG_PATH = "img/cards/revenant/PowerOfVengeance.png";
    private static final int COST = 1;
    private static final int BASE_BLOCK_TARGET = 5;
    private static final int KILL_INCREASE = 10;
    private static final int HP_LOSS_DECREASE = -5;

    public PowerOfVengeance() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.SELF);
        syncPermanentValue();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        syncPermanentValue();
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new PowerOfVengeancePower((AbstractCreature)p, this.uuid, this.magicNumber), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        syncPermanentValue();
        super.applyPowers();
        resetPermanentDisplay();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        syncPermanentValue();
        super.calculateCardDamage(mo);
        resetPermanentDisplay();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        syncPermanentValue();
        resetPermanentDisplay();
    }

    @Override
    public void onMoveToDiscard() {
        resetPermanentDisplay();
    }

    public void refreshPermanentValueDisplay() {
        syncPermanentValue();
        resetPermanentDisplay();
        initializeDescription();
    }

    private void syncPermanentValue() {
        this.misc = Math.max(-BASE_BLOCK_TARGET, this.misc);
        this.baseMagicNumber = getBlockTarget(this.misc);
        this.magicNumber = this.baseMagicNumber;
    }

    private void resetPermanentDisplay() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    public static int adjustPermanentBlockTarget(UUID cardUuid, int delta) {
        int target = -1;
        if (AbstractDungeon.player != null) {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card.uuid.equals(cardUuid)) {
                    target = adjustCard(card, delta);
                }
            }
        }
        for (AbstractCard card : GetAllInBattleInstances.get(cardUuid)) {
            target = adjustCard(card, delta);
        }
        return target;
    }

    public static int getBlockTarget(int misc) {
        return Math.max(0, BASE_BLOCK_TARGET + misc);
    }

    public static int getKillIncrease() {
        return KILL_INCREASE;
    }

    public static int getHpLossDecrease() {
        return HP_LOSS_DECREASE;
    }

    private static int adjustCard(AbstractCard card, int delta) {
        card.misc = Math.max(-BASE_BLOCK_TARGET, card.misc + delta);
        if (card instanceof PowerOfVengeance) {
            ((PowerOfVengeance)card).refreshPermanentValueDisplay();
        } else {
            card.applyPowers();
        }
        return getBlockTarget(card.misc);
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerOfVengeance();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
