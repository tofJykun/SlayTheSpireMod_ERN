package cards.guardian;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;

public class NeedlePiercer extends CustomCard {
    public static final String ID = "NeedlePiercer";
    private static final String IMG_PATH = "img/cards/guardian/NeedlePiercer.png";
    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public NeedlePiercer() {
        super(ID, STRINGS.NAME, IMG_PATH, 1, STRINGS.DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 7;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // Snapshot before UseCardAction triggers Vigor's removal.
        AbstractPower vigor = player.getPower(VigorPower.POWER_ID);
        int cardsToDraw = vigor == null ? 0 : Math.max(0, vigor.amount) * this.magicNumber;
        addToBot(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (cardsToDraw > 0) {
            addToBot(new DrawCardAction(player, cardsToDraw));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NeedlePiercer();
    }
}
