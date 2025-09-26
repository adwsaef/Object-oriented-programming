package randomGenerator;

import java.util.Random;

public class Losowanie {
    public static int losuj(int dolna, int gorna) {
        if (dolna == gorna)
            return dolna;
        Random random = new Random();
        return random.nextInt(gorna - dolna) + dolna;
    }
}
