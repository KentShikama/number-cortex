package com.numbercortex.logic;

import com.numbercortex.Persistence;

public class LogicConstructor {
    private LogicConstructor() {}
    
    public static boolean isCodeCorrect(String twoPlayerCode) {
        if (twoPlayerCode.equals("15x01")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void writeCorrectCode() {
        Persistence persistence = Persistence.getInstance();
        persistence.setTwoPlayerCode("15x01");
    }
}
