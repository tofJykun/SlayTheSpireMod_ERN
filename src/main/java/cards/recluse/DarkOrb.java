package cards.recluse;

import basemod.abstracts.CustomCard;
import cards.status.MagicEmber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class DarkOrb extends CustomCard {
    public static final String ID = "DarkOrb";
    private static final String IMG_PATH = "img/cards/recluse/DarkOrb.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int MAGIC_EMBERS = 1;

    public DarkOrb() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = this.magicNumber = MAGIC_EMBERS;
        this.cardsToPreview = new MagicEmber();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction((AbstractCreature)m,
                new DamageInfo((AbstractCreature)p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot((AbstractGameAction)new MakeTempCardInHandAction(new MagicEmber(), this.magicNumber));
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        AbstractCard returningCard = general.SmithingBody.physical(this);
        if (card != null && card.type == CardType.POWER
                && AbstractDungeon.player != null && AbstractDungeon.actionManager != null
                && AbstractDungeon.player.discardPile.contains(returningCard)) {
            addToBot(new DiscardToHandAction(returningCard));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new DarkOrb();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
