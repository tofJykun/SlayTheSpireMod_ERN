package cards.raider;

import actions.InsuranceReturnAction;
import basemod.abstracts.CustomCard;
import cards.tempcards.CraftmanCreation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingHelper;
import patches.AbstractCardEnum;
import patches.InsuranceField;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;
import powers.MasterworkPower;
import relics.FighterDestined;

public class BloodforgedSword extends CustomCard {
    public static final String ID = "BloodforgedSword";
    private static final String IMG_PATH = "img/cards/raider/BloodforgedSword.png";
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int GREY_HEALTH = 2;
    private AbstractCard pendingMaterial;

    public BloodforgedSword() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = GREY_HEALTH;
        this.magicNumber = this.baseMagicNumber;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        addToBot(new ApplyPowerAction(p, p, p.hasRelic(FighterDestined.ID)
                ? new GreyHealthPlusPower(p, this.magicNumber)
                : new GreyHealthPower(p, this.magicNumber), this.magicNumber));

        AbstractCard source = SmithingHelper.getSmithingSourceCard(this);
        if (source instanceof CraftmanCreation) {
            ((CraftmanCreation)source).reforgeAfterUse(SmithingHelper.randomReinforcementMaterial());
        } else {
            // Do not change this play's replay/exhaust flags or its already queued copies.
            this.pendingMaterial = SmithingHelper.randomSmithingMaterial();
        }
    }

    public void refreshAfterUseAction() {
        AbstractCard material = this.pendingMaterial;
        this.pendingMaterial = null;
        AbstractPlayer player = AbstractDungeon.player;
        if (material == null || player == null) {
            return;
        }
        if (this.purgeOnUse) {
            // Replayed copies still forge, but must not leave an extra card in a pile.
            MasterworkPower.onSmithing(player);
            return;
        }
        for (CardGroup pile : new CardGroup[] {player.hand, player.drawPile,
                player.discardPile, player.exhaustPile}) {
            int index = pile.group.indexOf(this);
            if (index < 0) {
                continue;
            }
            CraftmanCreation creation = new CraftmanCreation(
                    SmithingHelper.isInfusionMaterial(material) ? material : null, this,
                    SmithingHelper.isReinforcementMaterial(material) ? material : null);
            creation.uuid = this.uuid;
            creation.current_x = this.current_x;
            creation.current_y = this.current_y;
            creation.target_x = this.target_x;
            creation.target_y = this.target_y;
            InsuranceField.inherit(creation, this);
            pile.group.set(index, creation);
            if (pile == player.exhaustPile && InsuranceField.isInsured(creation)) {
                addToBot(new InsuranceReturnAction(player, creation));
            }
            player.hand.refreshHandLayout();
            player.hand.applyPowers();
            MasterworkPower.onSmithing(player);
            return;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        refreshDescription();
    }

    private void refreshDescription() {
        CardStrings strings = getCardStrings();
        String description = AbstractDungeon.player != null
                && AbstractDungeon.player.hasRelic(FighterDestined.ID)
                ? strings.EXTENDED_DESCRIPTION[0] : strings.DESCRIPTION;
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodforgedSword();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }
}
