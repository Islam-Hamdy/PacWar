package com.pacwar;

import java.util.ArrayList;

public class Player {
	int score;
	ArrayList<Man> men;
	int men_counter;
	int state;
	int pac_counter;

	public Player() {
		men = new ArrayList<Man>();
		men_counter = 0;
		pac_counter = 0;
	}

	public void addMan(Man man) {
		if (men_counter < Global.MAX_MEN_PLAYERS) {
			men.add(man);
			men_counter++;
			man.index = men_counter;
			if (man.type == Global.PACMAN_TYPE)
				pac_counter++;
		}
	}

	public boolean losePacMan(int index) {
		if (men_counter > 0) {
			men.remove(index);
			men_counter--;
			pac_counter--;
		}
		return pac_counter <= 0;
	}

}
