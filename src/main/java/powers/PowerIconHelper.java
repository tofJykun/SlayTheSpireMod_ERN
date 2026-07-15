package powers;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

class PowerIconHelper {
    private static final String BASE_PATH = "img/powers/";
    private static final String EXTENSION = ".png";

    private PowerIconHelper() {
    }

    static void load(AbstractPower power, String imageName) {
        power.img = ImageMaster.loadImage(BASE_PATH + imageName + EXTENSION);
    }
}
