package cards.recluse;

import actions.CometAzurAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.IntelligencePower;

public class CometAzur extends CustomCard {
    public static final String ID = "CometAzur";
    private static final String IMG_PATH = "img/cards/recluse/CometAzur.png";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = -1;
    private static final int ATTACK_DMG = 17;
    private static final int REQUIRED_INTELLIGENCE = 3;

    public CometAzur() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new CometAzurAction(p, m, this.damage, this.damageTypeForTurn,
                this.upgraded, this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (!hasEnoughIntelligence(p)) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = hasEnoughIntelligence(AbstractDungeon.player)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private boolean hasEnoughIntelligence(AbstractPlayer p) {
        return p != null
                && p.hasPower(IntelligencePower.POWER_ID)
                && p.getPower(IntelligencePower.POWER_ID).amount >= REQUIRED_INTELLIGENCE;
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new CometAzur();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
