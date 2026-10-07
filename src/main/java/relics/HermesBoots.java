package relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HermesBoots extends CustomRelic {
    public static final String ID = "HermesBoots";

    public HermesBoots() {
        super(ID, ImageMaster.loadImage("img/relics/scholar/HermesBoots.png"),
                ImageMaster.loadImage("img/relics/scholar/outline/HermesBoots.png"),
                RelicTier.SPECIAL, LandingSound.FLAT);
        setCounter(0);
    }

    public void addCharges(int amount) {
        if (amount > 0) {
            setCounter((int)Math.min(Integer.MAX_VALUE, (long)counter + amount));
            flash();
        }
    }

    public boolean consumeCharge() {
        if (counter <= 0) return false;
        setCounter(counter - 1);
        flash();
        return true;
    }

    @Override
    public void setCounter(int value) {
        counter = Math.max(0, value);
        grayscale = counter == 0;
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + Math.max(0, counter) + DESCRIPTIONS[1];
    }

    @Override
    public boolean canSpawn() { return false; }

    @Override
    public AbstractRelic makeCopy() { return new HermesBoots(); }
}
