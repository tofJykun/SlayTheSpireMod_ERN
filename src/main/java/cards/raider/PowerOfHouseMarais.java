package cards.raider;

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
import com.megacrit.cardcrawl.powers.StrengthPower;
import patches.AbstractCardEnum;
import powers.PowerOfHouseMaraisPower;

import java.util.UUID;

public class PowerOfHouseMarais extends CustomCard {
    public static final String ID = "PowerOfHouseMarais";
    private static final String IMG_PATH = "img/cards/raider/PowerOfHouseMarais.png";
    private static final int COST = 1;
    private static final int BASE_STRENGTH = 5;
    private static final int KILL_INCREASE = 5;
    private static final int HP_LOSS_DECREASE = -1;

    public PowerOfHouseMarais() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Raider_COLOR, CardRarity.RARE, CardTarget.SELF);
        syncPermanentValue();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        syncPermanentValue();
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new StrengthPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new PowerOfHouseMaraisPower((AbstractCreature)p, this.uuid, this.magicNumber), this.magicNumber));
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
        this.misc = Math.max(-BASE_STRENGTH, this.misc);
        this.baseMagicNumber = getStrength(this.misc);
        this.magicNumber = this.baseMagicNumber;
    }

    private void resetPermanentDisplay() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
    }

    public static int adjustPermanentStrength(UUID cardUuid, int delta) {
        int strength = -1;
        if (AbstractDungeon.player != null) {
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card.uuid.equals(cardUuid)) {
                    strength = adjustCard(card, delta);
                }
            }
        }
        for (AbstractCard card : GetAllInBattleInstances.get(cardUuid)) {
            strength = adjustCard(card, delta);
        }
        return strength;
    }

    public static int getStrength(int misc) {
        return Math.max(0, BASE_STRENGTH + misc);
    }

    public static int getKillIncrease() {
        return KILL_INCREASE;
    }

    public static int getHpLossDecrease() {
        return HP_LOSS_DECREASE;
    }

    private static int adjustCard(AbstractCard card, int delta) {
        card.misc = Math.max(-BASE_STRENGTH, card.misc + delta);
        if (card instanceof PowerOfHouseMarais) {
            ((PowerOfHouseMarais)card).refreshPermanentValueDisplay();
        } else {
            card.applyPowers();
        }
        return getStrength(card.misc);
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerOfHouseMarais();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
