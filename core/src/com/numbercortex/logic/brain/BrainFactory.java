package com.numbercortex.logic.brain;

import com.numbercortex.GameSettings;

public class BrainFactory {
	public static Brain buildBrain(GameSettings settings, int difficulty) {
		Brain brain;
		switch (difficulty) {
			case 1:
				brain = new RandomBrain(settings);
				break;
			case 2:
				brain = new EasyBrain(settings);
				break;
			case 3:
				brain = new MediumBrain(settings);
				break;
			case 4:
				brain = new HardBrain(settings);
				break;
			default:
				brain = new ImpossibleBrain(settings);
				break;
		}
		return brain;
	}
}
