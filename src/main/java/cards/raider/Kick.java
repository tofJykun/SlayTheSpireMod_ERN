package cards.raider;

import actions.RandomPlayHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import patches.AbstractCardEnum;

public class Kick extends CustomCard {
    public static final String ID = "Kick";
    private static final String IMG_PATH = "img/cards/raider/Kick.png";
    private static final int COST = 3;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Kick() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (AbstractDungeon.player == null
                || AbstractDungeon.player.hand == null
                || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.cardsPlayedThisTurn.size() >= 999
                || AbstractDungeon.getMonsters() == null
                || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            return;
        }
        // The draw callback runs before addToHand. Let native draw/use move the card;
        // adding it to limbo here leaves a second render reference after it is played.
        RandomPlayHelper.prepareRandomPlayedCard(this);
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(this, true,
                EnergyPanel.getCurrentEnergy(), true, true), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            return;
        }
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Kick();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
