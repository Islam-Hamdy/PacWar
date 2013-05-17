package com.pacwar;

import java.util.ArrayList;

public class Player {
	int score;
	ArrayList<Man> men;
	int men_counter;
	int state;

	public Player() {
		men = new ArrayList<Man>();
		men_counter = 0;
	}

	public void addMan(Man man) {
		if (men_counter < Global.MAX_MEN_PLAYERS) {
			men.add(man);
			men_counter++;
			man.index = men_counter;
		}
	}

}
