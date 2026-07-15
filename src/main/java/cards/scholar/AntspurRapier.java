package cards.scholar;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import patches.AbstractCardEnum;
import powers.ScarletRotPower;

public class AntspurRapier extends CustomCard {
    public static final String ID = "AntspurRapier";
    private static final String IMG_PATH = "img/cards/scholar/AntspurRapier.png";
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int SCARLET_ROT = 6;
    private static final int UPGRADE_PLUS_SCARLET_ROT = 2;

    public AntspurRapier() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = SCARLET_ROT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot((AbstractGameAction)new ApplyPowerAction(m, p,
                new ScarletRotPower(m, p, this.magicNumber), this.magicNumber));
        if (!this.purgeOnUse && p.hasPower(VigorPower.POWER_ID)) {
            queueExtraPlay(m);
        }
    }

    private void queueExtraPlay(AbstractMonster m) {
        AbstractCard copy = makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(copy);
        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        if (m != null) {
            copy.calculateCardDamage(m);
        }
        copy.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, m, this.energyOnUse, true, true), true);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDungeon.player != null && AbstractDungeon.player.hasPower(VigorPower.POWER_ID)
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public AbstractCard makeCopy() {
        return new AntspurRapier();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SCARLET_ROT);
            initializeDescription();
        }
    }
}
