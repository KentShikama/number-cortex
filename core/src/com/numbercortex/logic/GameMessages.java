package com.numbercortex.logic;

public class GameMessages {

	static String[] getTutorialMessage(int turnCount) {
		if (turnCount == 1) {
			return new String[] {
					"Welcome! Number Cortex is a game where you take turns with your opponent placing numbers (1 ~ 17, excluding 9) on the board.",
					"Go ahead and DRAG AND DROP the chosen number onto the board." };
		} else if (turnCount == 2) {
			return new String[] { "Number Cortex is unique because you get to choose which number your opponent will play next. DOUBLE TAP your opponents next number from the scroller below." };
		} else if (turnCount == 5) {
			return new String[] { "Your objective is to be the first one to make a 3-in-a-row (horizontally, vertically, or diagonally) of all evens, all odds, all single digits, or all double digits. Good luck!" };
		}
		return null;
	}

	static String getUnlockMessage(int currentLevel) {
		switch (currentLevel) {
			case 0:
				return "Adjustments unlocked! You can now adjust the placement of a number once. Simply drag the placed number to a different square.";
			case 3:
				return "New rule: Primes and Composites! Adding to the current ruleset, you can make a 3-in-a-row of all primes (1*, 3, 5, 7, 9, 11, 13, 15, 17) or all composites (2, 4, 6, 8, 10, 12, 14, 16).";
			case 6:
				return "New rule: Middles and Edges! Adding to the current ruleset, you can make a 3-in-a-row of all middles (5 ~ 12) or all edges (1 ~ 4 and 13 ~ 17).";
			case 9:
				return "Board size increase! You must now make a 4-in-a-row of numbers of the same attribute in order to win.";
			case 13:
				return "Four squares unlocked! You may now make a 4-in-a-row by placing a set of winning numbers in a 2 x 2 square.";
		}
		return null;
	}

}
