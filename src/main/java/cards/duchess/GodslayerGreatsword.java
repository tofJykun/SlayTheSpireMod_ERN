package cards.duchess;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractCardEnum;
import powers.BloodburnPower;

import java.util.Set;

public class GodslayerGreatsword extends CustomCard {
    public static final String ID = "GodslayerGreatsword";
    private int bloodburnApplications = 1;

    public GodslayerGreatsword() {
        super(ID, CardCrawlGame.languagePack.getCardStrings(ID).NAME,
                "img/cards/duchess/GodslayerGreatsword.png", 3,
                CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.Duchess_COLOR, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 13;
        baseMagicNumber = magicNumber = 13;
        isEthereal = true;
    }

    public int getBloodburnApplications() {
        return bloodburnApplications;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Set<AbstractCard> instances = GetAllInBattleInstances.get(uuid);
        instances.add(this);
        int applications = bloodburnApplications;
        for (AbstractCard card : instances) {
            if (card instanceof GodslayerGreatsword) {
                applications = Math.max(applications, ((GodslayerGreatsword)card).bloodburnApplications);
            }
        }
        // Same-UUID replays share growth; separate physical cards and the master deck do not.
        int next = applications == Integer.MAX_VALUE ? applications : applications + 1;
        for (AbstractCard card : instances) {
            if (card instanceof GodslayerGreatsword) {
                ((GodslayerGreatsword)card).bloodburnApplications = next;
                card.initializeDescription();
            }
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        for (int i = 0; i < applications; i++) {
            addToBot(new ApplyPowerAction(m, p, new BloodburnPower(m, p, magicNumber), magicNumber));
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        GodslayerGreatsword copy = (GodslayerGreatsword)super.makeStatEquivalentCopy();
        copy.bloodburnApplications = bloodburnApplications;
        copy.initializeDescription();
        return copy;
    }

    @Override
    public AbstractCard makeCopy() {
        return new GodslayerGreatsword();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }
}
