package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import patches.AbstractCardEnum;
import powers.GreyHealthPlusPower;
import powers.GreyHealthPower;
import relics.FighterDestined;

public class DuelistGreataxe extends CustomCard {
    public static final String ID = "DuelistGreataxe";
    private static final String IMG_PATH = "img/cards/raider/DuelistGreataxe.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 16;
    private static final int UPGRADE_PLUS_DMG = 6;
    private static final int GREY_HEALTH_THRESHOLD = 6;

    public DuelistGreataxe() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (!this.purgeOnUse && hasEnoughGreyHealth(p)) {
            queueExtraPlay(m);
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

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = hasEnoughGreyHealth(AbstractDungeon.player)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private void queueExtraPlay(AbstractMonster m) {
        AbstractCard copy = makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(copy);
        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.calculateCardDamage(m);
        copy.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, m, this.energyOnUse, true, true), true);
    }

    private void refreshDescription() {
        CardStrings cardStrings = getCardStrings();
        String description = hasFighterDestined(AbstractDungeon.player)
                ? cardStrings.EXTENDED_DESCRIPTION[0]
                : cardStrings.DESCRIPTION;
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    private static boolean hasEnoughGreyHealth(AbstractPlayer player) {
        if (player == null) {
            return false;
        }
        String powerId = hasFighterDestined(player) ? GreyHealthPlusPower.POWER_ID : GreyHealthPower.POWER_ID;
        AbstractPower power = player.getPower(powerId);
        return power != null && power.amount >= GREY_HEALTH_THRESHOLD;
    }

    private static boolean hasFighterDestined(AbstractPlayer player) {
        return player != null && player.hasRelic(FighterDestined.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new DuelistGreataxe();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
