package com.aprivate.sean.ohcg;

import java.util.Random;

public class Utility {
    public static int getRandomInt() {
        if(random == null){
            random = new Random();
        }
        return random.nextInt(Integer.MAX_VALUE);
    }

    private static Random random;


}
