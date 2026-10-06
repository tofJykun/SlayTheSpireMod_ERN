package cards.revenant;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.SmithingBody;
import patches.AbstractCardEnum;

public class WraithCallingBell extends CustomCard {
    public static final String ID = "WraithCallingBell";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public WraithCallingBell() {
        this(true);
    }

    private WraithCallingBell(boolean preview) {
        super(ID, STRINGS.NAME, "img/cards/revenant/WraithCallingBell.png", 0, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Revenant_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 3;
        if (preview) cardsToPreview = new WraithCallingBell(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard card) {
        AbstractCard returning = SmithingBody.physical(this);
        AbstractCard played = SmithingBody.physical(card);
        if (played == null || played == returning || ID.equals(SmithingBody.behavior(played).cardID)) return;
        boolean zeroCost = played.cost == -1 ? played.energyOnUse == 0 : played.costForTurn == 0;
        if (zeroCost && AbstractDungeon.player != null && AbstractDungeon.actionManager != null
                && AbstractDungeon.player.discardPile.contains(returning)) {
            AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(returning));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            if (cardsToPreview != null) cardsToPreview.upgrade();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WraithCallingBell();
    }
}
