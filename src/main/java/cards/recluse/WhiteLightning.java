package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import patches.AbstractCardEnum;
import powers.WhiteLightningPower;

public class WhiteLightning extends CustomCard {
    public static final String ID = "WhiteLightning";
    private static final String IMG_PATH = "img/cards/recluse/WhiteLightning.png";
    private static final int COST = 3;
    private static final int ATTACK_DMG = 137;
    private static final int VULNERABLE = 2;
    private static final int UPGRADE_PLUS_VULNERABLE = -1;
    private static final int DELAY_TURNS = 3;

    public WhiteLightning() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.Recluse_COLOR, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.baseMagicNumber = VULNERABLE;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new VulnerablePower((AbstractCreature)p, this.magicNumber, false), this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new WhiteLightningPower((AbstractCreature)p, DELAY_TURNS, this.damage), DELAY_TURNS));
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new WhiteLightning();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
        }
    }
}
