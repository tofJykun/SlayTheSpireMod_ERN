package cards.duchess;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.DarkWoodGrainPower;

import java.util.Collections;
import java.util.List;

public class DarkWoodGrain extends CustomCard {
    public static final String ID = "DarkWoodGrain";
    private static final String IMG_PATH = "img/cards/duchess/DarkWoodGrain.png";
    private static final int COST = 1;
    private static final int AMOUNT = 1;

    public DarkWoodGrain() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.POWER, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p,
                new DarkWoodGrainPower((AbstractCreature)p, this.magicNumber), this.magicNumber));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            return Collections.singletonList(new TooltipInfo("定时",
                    "如果你在这回合打出的牌数少于999张，这张牌将在打出 #bN 张牌后自动打出。在这张牌进入或离开手牌时重置到 #bM 。"));
        }
        return Collections.singletonList(new TooltipInfo("Scheduled",
                "If you have played fewer than 999 cards this turn, this card is automatically played after you play #bN cards. Reset to #bM whenever this card enters or leaves your hand."));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarkWoodGrain();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = getCardStrings().UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
