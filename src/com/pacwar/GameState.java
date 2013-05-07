package com.pacwar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.content.res.AssetManager;
import android.graphics.Point;

public class GameState implements Runnable {
	public static MainActivity view;
	static int ghosts = 2, pacmans = 2;
	static int map_w = Global.MAP_WIDTH, map_h = Global.MAP_HEIGHT;
	float screen_w, screen_h;

	// men 1,2,3,4,5,6...
	// player1 +ve, player2 -ve
	static final int block = 0;
	static final int pac = 1 << 20;
	static final int select = 0;
	static final int action = 1;
	int clickState = 0;
	static boolean[][] map = new boolean[map_h][map_w];
	static boolean[][] vis = new boolean[map_h][map_w];
	int pl_1_state, pl_2_state;
	static float cell_w, cell_h;

	ArrayList<Man> pl1_men = new ArrayList<Man>();
	ArrayList<Man> pl2_men = new ArrayList<Man>();

	public GameState(MainActivity mainActivity) throws IOException {
		view = mainActivity;
		init(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
		new Thread(this).start();
	}

	public void addMan(Man man) {
		pl1_men.add(man);
		int idx = pl1_men.size();
		man.index = idx;
	}

	public void init(float screen_w, float screen_h) throws IOException {
		this.screen_w = screen_w;
		this.screen_h = screen_h;
		cell_w = (screen_w / map_w);
		cell_h = (screen_h / map_h);
		Global.CELL_WIDTH = cell_w;
		Global.CELL_HEIGHT = cell_h;
		Global.TOUCH_ERROR_THRESHOLD = Math.max(Global.CELL_HEIGHT,
				Global.CELL_WIDTH);
		pl1_men.clear();
		pl2_men.clear();

		mapinit();
		pl_1_state = select;
		pl_2_state = select;

		Man pm = new Man();
		pm.x = cell_w;
		pm.y = cell_h;
		pm.destX = pm.x;
		pm.destY = pm.y;
		pm.cenX = (pm.x + 1.5f * cell_w);
		pm.cenY = (pm.y + 1.5f * cell_h);
		addMan(pm);
		view.show_pacman(0, (int) pm.x, (int) pm.y, (int) cell_w * 3,
				(int) cell_h * 3);
	}

	public void mapinit() throws IOException {
		AssetManager am = view.getAssets();
		InputStream is = am.open("initial_map.txt");
		BufferedReader buff = new BufferedReader(new InputStreamReader(is));
		for (int i = 0; i < Global.MAP_HEIGHT; i++) {
			String row = buff.readLine();
			for (int j = 0; j < row.length(); j++) {
				if (row.charAt(j) == '.')
					map[i][j] = true;
			}
		}
		buff.close();
	}

	class state {
		int x, y, d;
		state p;

		public state(int x, int y) {
			this.x = x;
			this.y = y;
			d = 0;
			p = null;
		}

		public state(int x, int y, state pp, int dd) {
			this.x = x;
			this.y = y;
			p = pp;
			d = dd;
		}
	}

	int[] dx = { 0, 0, 1, -1, 1, 1, -1, -1 };
	int[] dy = { 1, -1, 0, 0, -1, 1, 1, -1 };

	// x1,y1,x2,y2 are the indices on map NOT pixels
	// x1,x2 HORIZONTAL map[0].length
	// y1,y2 VERTICAL map.length
	public Point[] findPath(int x1, int y1, int x2, int y2) {
		Queue<state> q = new LinkedList<state>();
		state cur = null;
		q.add(new state(x1, y1));
		for (int i = 0; i < vis.length; i++)
			for (int j = 0; j < vis[0].length; j++)
				vis[i][j] = false;
		int nx, ny;
		while (!q.isEmpty()) {
			cur = q.poll();
			if (cur.x == x2 && cur.y == y2)
				break;
			for (int k = 0; k < 4; k++) {
				nx = cur.x + dx[k];
				ny = cur.y + dy[k];
				if (nx < 0 || nx >= map[0].length || ny < 0 || ny >= map.length
						|| vis[nx][ny] || !map[nx][ny])
					continue;
				vis[cur.x][cur.y] = true;
				q.add(new state(nx, ny, cur, cur.d + 1));
			}
		}
		Point[] ret = new Point[cur.d];
		while (cur.p != null) {
			ret[cur.d - 1] = new Point(cur.x, cur.y);
			cur = cur.p;
		}
		return ret;
	}

	int selectedMan;

	public void SceneTouch(float x, float y) {
		int yMap = (int) (x / Global.CELL_WIDTH);
		int xMap = (int) (y / Global.CELL_HEIGHT);

		int manClicked = pac;
		float min = 1 << 27, tmp;
		Man cur;
		for (int i = 0; i < pl1_men.size(); i++) {
			cur = pl1_men.get(i);
			tmp = (cur.cenX - x) * (cur.cenX - x) + (cur.cenY - y)
					* (cur.cenY - y);
			if (tmp < min
					&& tmp < Global.TOUCH_ERROR_THRESHOLD
							* Global.TOUCH_ERROR_THRESHOLD) {
				min = tmp;
				manClicked = cur.index;
			}
		}

		System.out.println("pos " + x + " " + y);
		System.out.println(xMap + "   ?   " + yMap);
		System.out.println(1 + " " + clickState);
		if (manClicked > 0 && manClicked != pac
				&& manClicked - 1 == selectedMan && clickState == action) {
			return;
		}
		if (clickState == select) {
			if (manClicked > 0 && manClicked != pac) {
				selectedMan = manClicked - 1;
				clickState = action;
			}
		} else if (clickState == action) {
			if (!map[xMap][yMap])
				return;
			Man man = pl1_men.get(selectedMan);
			Point p = getPoint(x, y);
			man.next = findPath((int)(man.y/cell_h), (int)(man.x/cell_w), p.x, p.y);
			man.current_point_to_go = 1;
			man.destX = man.next[0].y * Global.CELL_WIDTH;
			man.destY = man.next[0].x * Global.CELL_HEIGHT;
			clickState = select;
		}
		System.out.println(2 + " " + clickState);
	}

	public Point getPoint(float x, float y) {
		int yMap = (int) (x / Global.CELL_WIDTH);
		int xMap = (int) (y / Global.CELL_HEIGHT);
		System.out.println("=============" + xMap + "   " + yMap);
		if (map[xMap][yMap])
			return new Point(xMap, yMap);
		int nx, ny;
		for (int i = 0; i < 8; i++) {
			nx = xMap + dx[i];
			ny = yMap + dy[i];
			if (nx < 0 || nx >= map.length || ny < 0 || ny >= map[0].length)
				continue;
			if (map[nx][ny])
				return new Point(nx, ny);
		}
		return null; // if we are here then a7a :|
	}

	public void changeScreenSize(int sizeX, int sizeY) {
		// view.windowSizeChanged(sizeX, sizeY);
	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < pl1_men.size(); i++) {
				pl1_men.get(i).updateMe();
				view.move_pacman(pl1_men.get(i).color, (int) pl1_men.get(i).x,
						(int) pl1_men.get(i).y);
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}