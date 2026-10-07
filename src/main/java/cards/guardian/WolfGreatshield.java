package cards.guardian;

import basemod.abstracts.CustomCard;
import cards.wylder.WylderIntentHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

public class WolfGreatshield extends CustomCard {
    public static final String ID = "WolfGreatshield";
    private static final String IMG_PATH = "img/cards/guardian/WolfGreatshield.png";
    private static final int COST = 3;

    public WolfGreatshield() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Guardian_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 0;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseBlock = WylderIntentHelper.totalIncomingAttackDamage(p);
        super.applyPowers();
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)p, (AbstractCreature)p, this.block));
    }

    @Override
    public void applyPowers() {
        this.baseBlock = AbstractDungeon.player == null
                ? 0
                : WylderIntentHelper.totalIncomingAttackDamage(AbstractDungeon.player);
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseBlock = AbstractDungeon.player == null
                ? 0
                : WylderIntentHelper.totalIncomingAttackDamage(AbstractDungeon.player);
        super.calculateCardDamage(mo);
    }

    @Override
    public AbstractCard makeCopy() {
        return new WolfGreatshield();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.selfRetain = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
