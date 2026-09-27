package cards.recluse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import general.CombatState;
import patches.AbstractCardEnum;
import powers.HiddenBodyPower;

public class HiddenBody extends CustomCard {
    public static final String ID = "HiddenBody";
    private static final String IMG_PATH = "img/cards/recluse/HiddenBody.png";
    private static final int COST = 0;
    private static final int WEAK = 3;
    private static final int UPGRADE_PLUS_WEAK = 2;

    public HiddenBody() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.Recluse_COLOR, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = WEAK;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        refreshDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!CombatState.isInCombat() || AbstractDungeon.getMonsters() == null) {
            return;
        }
        int weak = effectiveWeak();
        if (weak <= 0) {
            return;
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && !monster.halfDead) {
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, weak, false), weak));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return (AbstractCard)new HiddenBody();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            refreshDescription();
        }
    }

    private void refreshDescription() {
        CardStrings strings = getCardStrings();
        int effectiveWeak = effectiveWeak();
        String description = (this.upgraded ? strings.UPGRADE_DESCRIPTION : strings.DESCRIPTION)
                .replace("!MX!", "1")
                .replace("!M!", Integer.toString(effectiveWeak));
        if (!description.equals(this.rawDescription)) {
            this.rawDescription = description;
            initializeDescription();
        }
    }

    private int effectiveWeak() {
        return Math.max(0, this.baseMagicNumber - HiddenBodyPower.countAttackingEnemies());
    }

    @Override
    public void update() {
        super.update();
        refreshDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        refreshDescription();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        refreshDescription();
    }
}
