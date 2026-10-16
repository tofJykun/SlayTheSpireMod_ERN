package cards.scholar;

import actions.GainGoldWithAnimationAction;
import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;

@NoPools
public class BrandNewWorld extends CustomCard {
    public static final String ID = "BrandNewWorld";
    public static final int GOLD = 7;

    public BrandNewWorld() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/scholar/BrandNewWorld.png", 1, CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Scholar_COLOR, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        final int maxHpGain = magicNumber;
        addToBot(new ExpertiseAction(player, BaseMod.MAX_HAND_SIZE));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (isDone) return;
                isDone = true;
                player.increaseMaxHp(maxHpGain, true);
            }
        });
        addToBot(new GainEnergyAction(3));
        addToBot(new GainGoldWithAnimationAction(GOLD));
    }

    @Override
    public void upgrade() {
        if (!upgraded) { upgradeName(); upgradeBaseCost(0); }
    }

    @Override
    public AbstractCard makeCopy() { return new BrandNewWorld(); }
}
