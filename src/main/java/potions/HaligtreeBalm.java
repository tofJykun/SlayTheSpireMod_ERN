package potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import general.CombatState;
import general.PotionEffectHelper;
import powers.HaligtreeBalmPower;

public class HaligtreeBalm extends AbstractPotion {
    public static final String POTION_ID = "HaligtreeBalm";
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public HaligtreeBalm() {
        super(POTION_STRINGS.NAME, POTION_ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.WHITE);
        this.isThrown = false;
        this.targetRequired = false;
    }

    @Override
    public void initializeData() {
        this.potency = getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractPlayer player = AbstractDungeon.player;
        if (CombatState.isInCombat() && player != null) {
            addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)player,
                    (AbstractCreature)player, new HaligtreeBalmPower((AbstractCreature)player), 1));
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    PotionEffectHelper.refreshPlayerPotions();
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public int getPotency() {
        return getPotency(AbstractDungeon.ascensionLevel);
    }

    @Override
    public AbstractPotion makeCopy() {
        return new HaligtreeBalm();
    }
}
