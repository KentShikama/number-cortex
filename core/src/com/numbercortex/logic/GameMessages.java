package com.numbercortex.logic;

class GameMessages {

    static String[] getTutorialMessage(int turnCount) {
        if (turnCount == 0) {
            return new String[] { "Welcome to Number Cortex's interactive tutorial!", "In Number Cortex, you take turns with your opponent placing numbers on the board (1 ~ 17, excluding 9).", "Number Cortex is unique because you get to choose which number your opponent will play next.",
                    "TAP your opponent's next number from the scroller below." };
        } else if (turnCount == 3) {
            return new String[] { "Your opponent (AI) has just chosen a number for you (top right).", "DRAG AND DROP\nthe chosen number onto the board." };
        } else if (turnCount == 4) {
            return new String[] { "Your objective is to be the first one to make a 3-in-a-row (horizontally, vertically, or diagonally) of all evens, all odds, all single digits, or all double digits." };
        } else if (turnCount == 7) {
            return new String[] { "Note that you and your opponent share the same set of numbers. Good luck!" };
        }
        return null;
    }

    static String getUnlockMessage(int currentLevel) {
        switch (currentLevel) {
            case 0:
                return "Adjustments unlocked! You can now adjust the placement of a number once. Simply drag the placed number to a different square.";
            case 3:
                return "New rule: Primes and Composites! Adding to the current ruleset, you can make a 3-in-a-row of all primes (1*, 2, 3, 5, 7, 11, 13, 17) or all composites (4, 6, 8, 10, 12, 14, 15, 16).";
            case 6:
                return "New rule: Middles and Edges! Adding to the current ruleset, you can make a 3-in-a-row of all middles (5 ~ 13) or all edges (1 ~ 4 and 14 ~ 17).";
            case 9:
                return "Board size increase! You must now make a 4-in-a-row of numbers of the same attribute in order to win.";
            case 13:
                return "Four squares unlocked! You may now make a 4-in-a-row by placing a set of winning numbers in a 2 x 2 square.";
            case 18:
                return "Red background unlocked! Long press the options button below to toggle the game screen background color.";
        }
        return null;
    }
    
    static final String SHARE_MESSAGE_ENDING = "Would you like to share this achievement on Facebook?";

    static String[] getClearShareMessage(int currentLevel) {
        switch (currentLevel) {
//            case 0:
//                return new String[] { "Level 1 cleared! " + SHARE_MESSAGE_ENDING, "Level 1 Cleared!" };
            case 5:
                return new String[] { "Level 5 cleared! " + SHARE_MESSAGE_ENDING, "Level 5 Cleared!" };
            case 8:
                return new String[] { "Level 8 cleared! " + SHARE_MESSAGE_ENDING, "Level 8 Cleared!" };
            case 12:
                return new String[] { "Level 12 cleared! " + SHARE_MESSAGE_ENDING, "Level 12 Cleared!" };
            case 16:
                return new String[] { "Level 16 cleared! " + SHARE_MESSAGE_ENDING, "Level 16 Cleared!" };
            case 18:
                return new String[] { "Game cleared! " + SHARE_MESSAGE_ENDING, "Entire Game Cleared!" };
        }
        return null;
    }
    
    static String[] getTieShareMessage(int tieCount) {
        switch (tieCount) {
            case 1:
                return new String[] { "First tie! " + SHARE_MESSAGE_ENDING, "First Tie!", "Close but not quite." };
            case 3:
                return new String[] { "Third tie! " + SHARE_MESSAGE_ENDING, "Third Tie!", "The fact that you are tying this often signifies that you are on the right track." };
            case 8:
                return new String[] { "Eighth tie! " + SHARE_MESSAGE_ENDING, "Tie Master!", "Only the most patient of players can get this far." };
        }
        return null;
    }
    
    static String[] getLossShareMessage(int tieCount) {
        switch (tieCount) {
            case 7:
                return new String[] { "Seventh loss! " + SHARE_MESSAGE_ENDING, "7 losses!", "The line between those who quit and those who continue." };
            case 30:
                return new String[] { "30th loss! " + SHARE_MESSAGE_ENDING, "The Posseser of Resilience!", "Arguably the greatest virtue. Despite your 30 losses, you have refused to give up." };
            case 100:
                return new String[] { "100th loss! " + SHARE_MESSAGE_ENDING, "100 losses!", "Arguably the greatest achievement one can earn in Number Cortex." };
        }
        return null;
    }

}
