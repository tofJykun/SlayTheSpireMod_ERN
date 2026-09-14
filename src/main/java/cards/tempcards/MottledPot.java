package cards.tempcards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import general.CrudeDrug;
import powers.PotentateCookbookPower;

import java.util.ArrayList;
import java.util.List;

public class MottledPot extends CustomCard {
    public static final String ID = "MottledPot";
    private static final String IMG_PATH = "img/cards/tempcards/MottledPot.png";
    private static final int COST = 1;

    private final ArrayList<String> drugPowerIds = new ArrayList<>();

    public MottledPot() {
        super(ID, getCardStrings().NAME, IMG_PATH, COST, getCardStrings().DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        syncDescription();
    }

    public MottledPot(List<String> drugPowerIds) {
        this();
        this.drugPowerIds.addAll(drugPowerIds);
        syncDescription();
    }

    private static CardStrings getCardStrings() {
        return CardCrawlGame.languagePack.getCardStrings(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (String drugPowerId : this.drugPowerIds) {
            CrudeDrug.applyDrugEffect(drugPowerId, p, m, this);
        }
    }

    private void syncDescription() {
        this.exhaust = !PotentateCookbookPower.isActive();
        if (this.drugPowerIds.isEmpty()) {
            this.rawDescription = this.exhaust ? getCardStrings().DESCRIPTION : withoutExhaust(getCardStrings().DESCRIPTION);
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.drugPowerIds.size(); i++) {
                if (i > 0) {
                    builder.append(" NL ");
                }
                builder.append(CrudeDrug.getPotDescription(this.drugPowerIds.get(i)));
            }
            if (this.exhaust) {
                builder.append(exhaustDescription());
            }
            this.rawDescription = builder.toString();
        }
        initializeDescription();
    }

    public void refreshCookbookStatus() {
        syncDescription();
    }

    private String exhaustDescription() {
        CardStrings strings = getCardStrings();
        if (strings.EXTENDED_DESCRIPTION == null || strings.EXTENDED_DESCRIPTION.length == 0) {
            return "";
        }
        return strings.EXTENDED_DESCRIPTION[0];
    }

    private String withoutExhaust(String description) {
        String exhaustText = exhaustDescription();
        if (exhaustText.isEmpty() || description == null) {
            return description;
        }
        return description.replace(exhaustText, "");
    }

    @Override
    public AbstractCard makeCopy() {
        return new MottledPot(this.drugPowerIds);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        MottledPot card = (MottledPot)super.makeStatEquivalentCopy();
        card.drugPowerIds.clear();
        card.drugPowerIds.addAll(this.drugPowerIds);
        card.syncDescription();
        return card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}
