package cards.raider;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import patches.ReplayField;

public class WingedKnightTwinaxes extends CustomCard {
    public static final String ID = "WingedKnightTwinaxes";
    private static final String IMG_PATH = "img/cards/raider/WingedKnightTwinaxes.png";
    private static final int COST = 3;
    private static final int DAMAGE = 6;
    private static final int HIT_COUNT = 2;
    private static final int STARTING_REPLAY = 1;
    private static final int UPGRADE_PLUS_DMG = 2;

    public WingedKnightTwinaxes() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Raider_COLOR, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = STARTING_REPLAY;
        this.magicNumber = this.baseMagicNumber;
        ReplayField.setReplay(this, this.baseMagicNumber);
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HIT_COUNT; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        if (!ReplayField.isReplayCopy(this)) {
            addToBot(new IncreaseReplayAction(this));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WingedKnightTwinaxes();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    private static class IncreaseReplayAction extends AbstractGameAction {
        private final AbstractCard card;

        private IncreaseReplayAction(AbstractCard card) {
            this.card = card;
        }

        @Override
        public void update() {
            if (this.card != null) {
                int replay = ReplayField.getReplay(this.card) + 1;
                ReplayField.setReplay(this.card, replay);
                this.card.baseMagicNumber = replay;
                this.card.magicNumber = replay;
                this.card.isMagicNumberModified = this.card.magicNumber != STARTING_REPLAY;
                this.card.initializeDescription();
            }
            this.isDone = true;
        }
    }
}
