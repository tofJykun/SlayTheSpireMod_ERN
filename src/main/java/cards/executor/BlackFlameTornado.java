package cards.executor;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

public class BlackFlameTornado extends CustomCard {
    public static final String ID = "BlackFlameTornado";
    private static final String IMG_PATH = "img/cards/executor/BlackFlameTornado.png";
    private static final int COST = 2;
    private static final int BLOODBURN = 5;
    private static final int UPGRADE_PLUS_BLOODBURN = 2;

    public BlackFlameTornado() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.Executor_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = BLOODBURN;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int extraPlays = countHighHealthEnemies();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)monster, (AbstractCreature)p,
                        new BloodburnPower((AbstractCreature)monster, (AbstractCreature)p, this.magicNumber),
                        this.magicNumber, AbstractGameAction.AttackEffect.POISON));
            }
        }
        if (!this.purgeOnUse) {
            for (int i = 0; i < extraPlays; i++) {
                queueExtraPlay();
            }
        }
    }

    private int countHighHealthEnemies() {
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().monsters == null) {
            return 0;
        }
        int count = 0;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDeadOrEscaped() && monster.currentHealth >= monster.maxHealth * 0.5F) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = countHighHealthEnemies() > 0
                ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy()
                : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    private void queueExtraPlay() {
        AbstractCard copy = makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(copy);
        copy.current_x = this.current_x;
        copy.current_y = this.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.purgeOnUse = true;
        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, null, this.energyOnUse, true, true), true);
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackFlameTornado();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOODBURN);
            initializeDescription();
        }
    }
}
